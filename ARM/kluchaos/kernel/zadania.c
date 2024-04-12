#include "../kernel/magazyn.h"
#define	T0_BASE				0xE0004000
#define	IR_OFS				0x00
#define	TCR_OFS				0x04
#define	TC_OFS				0x08
#define	PR_OFS				0x0C
#define	MCR_OFS				0x14
#define	MR0_OFS				0x18
#define	VIC_BASE			0xFFFFF000
#define	VectAddr_OFS		0x030
#define	VectAddr0_OFS		0x100
#define	VectCntl0_OFS		0x200
#define	IntEnable_OFS		0x010

#define	PRIORYTET_NAJWYZSZY	5
#define	CZAS_PODSTAWA		PRIORYTET_NAJWYZSZY+1	// CZAS_PODSTAWA-PRIORYTET
#define	CZAS_MNOZNIK		4	// 2^CZAS_MNOZNIK
#define	ILOSC_ZADAN			30
#define WIELKOSC_STOSU		68
#define DLUGOSC_NAZWY		16
// calkowity czas planisty
#define CZAS_PLANISTY		0x40001000
// adres aktualnie wykonywanego zadania na liscie
#define	BIEZACE_ZADANIE		CZAS_PLANISTY+4
// koniec listy zadan
#define	OSTATNIE_ZADANIE	BIEZACE_ZADANIE+4
// lista zadan ktore maja sie wykonywac
#define	LISTA_ZADAN			OSTATNIE_ZADANIE+4
// gniazda na srodowiska zadan: rejestry, stosy, stany, czasy, priorytety
#define	ZRZUT_SRODOWISKA	LISTA_ZADAN+(ILOSC_ZADAN+1)*4

#define	TRYB_SYSTEMOWY		0x0000001f

// rozpoczecie pracy planisty, ustawienie timer0,
// dodanie zadania bezczynnosci na wypadek uruchomienia
// planisty bez zadnych zadan (w przeciwnym wypadku
// nastpiloby zresetowanie ukladu poprzez skok pod adres 0),
// i zaladowanie pierwszego zadania
__asm void planista_uruchom(void){
	// ustawienie vic dla timer0
	ldr r12, =VIC_BASE
	// po wyznaczonym czasie uruchamiany bedzie planista i nastapi
	// zmiana kontekstu
	ldr r11, =praca_planisty
	str r11, [r12, #VectAddr0_OFS]
	ldr r11, =(0x20|4)
	str r11, [r12, #VectCntl0_OFS]
	ldr r11, =0x10
	str r11, [r12, #IntEnable_OFS]
	// zadanie bezczynnosci jako pierwsze wykonywane
	ldr r12, =LISTA_ZADAN
	ldr r11, =BIEZACE_ZADANIE
	str r12, [r11]
	ldr r0, =4+4+17*4+DLUGOSC_NAZWY+WIELKOSC_STOSU
	IMPORT r_start
	bl r_start
	ldr r12, =LISTA_ZADAN
	str r0, [r12]
	// utworzenie nazwijmy to gniazda dla zadania bezczynnosci
	ldr r11, =4+4+17*4+DLUGOSC_NAZWY+WIELKOSC_STOSU
	add r11, r0, r11
	mov sp, r11
	// w czasie bezczynnosci wykonywana bedzie petla nieskonczona
	ldr lr, =pu_nic
	// zapisanie priorytetu dla tego zadania
	ldr r9, =1
	str r9, [r0], #4
	// konfiguracja timer0
	ldr r12, =T0_BASE
	// przydzielenie czasu procesora
	ldr r11, =0
	str r11, [r12, #PR_OFS]
	ldr r11, =5
	str r11, [r12, #MCR_OFS]
	ldr r11, =1
	str r11, [r12, #MR0_OFS]
	str r11, [r12, #TCR_OFS]
	bx lr
pu_nic b pu_nic
	;
// kod odpowiedzialny za zmiane kontkstu
praca_planisty str r12, [sp, #-4]!
			   // pobranie adresu gniazda wlasnie wykonywanego
			   // zadania
			   ldr r12, =BIEZACE_ZADANIE
			   ldr r12, [r12]
			   ldr r12, [r12]
			   add r12, r12, #8
			   stmia r12, {r0-r11}
			   ldr r11, [sp], #4
			   str r11, [r12, #12*4]
			   ldr r11, =T0_BASE+TC_OFS
			   ldr r11, [r11]
			   ldr r10, [r12, #-4]
			   add r10, r10, r11
			   str r10, [r12, #-4]
			   ldr r10, =CZAS_PLANISTY
			   ldr r9, [r10]
			   add r9, r9, r11
			   add r9, r9, #41 ;42
			   str r9, [r10]
			   
	
	;#####KOD UNIWERSALNY!!! NIE KASOWAC!!!#####
	// zapisanie srodowiska
	// rejestrow
	
	
	
	// nastepnej instrukcji do wykonania
	sub lr, lr, #4
	str lr, [r12, #15*4]
	// stanu procesora
	mrs r11, spsr
	str r11, [r12, #16*4]
	// przelaczenie do trybu systemowego aby dostac sie do
	// rejestrow sp i lr
	mrs r11, cpsr
	orr r10, r11, #TRYB_SYSTEMOWY
	msr cpsr_c, r10
	str sp, [r12, #13*4]
	str lr, [r12, #14*4]
	;#####KOD UNIWERSALNY!!! NIE KASOWAC!!!#####

	// wyszukanie nastepnego zadania
	ldr r10, =BIEZACE_ZADANIE
	ldr r12, [r10]
	// lista jednokierunkowa wiec to jest jej koniec
	ldr r9, =OSTATNIE_ZADANIE
	ldr r9, [r9]
// jesli wykonywane bylo ostatnie to zaczynamy od poczatku
	cmp r12, r9
	addlt r12, r12, #4
	ldrge r12, =LISTA_ZADAN
// jak wczesniej wyznaczamy czas, zwiekszamy liczniki
	str r12, [r10]
	ldr r12, [r12]
	ldr r10, [r12], #8
	;ldr r9, =CZAS_PRIORYTETOW
	;mov r10, r9, asr r10
	rsb r10, r10, #CZAS_PODSTAWA
	lsl r10, #CZAS_MNOZNIK
	ldr r9, =T0_BASE
	str r10, [r9, #MR0_OFS]
	
	// i w koncu ladujemy poprzedni stan nastepnego zadania
	;#####KOD UNIWERSALNY!!! NIE KASOWAC!!!#####
	// skoro nadal jestesmy w trybie sytemowym to zaczniemy
	// od sp i lr
	ldr sp, [r12, #13*4]
	ldr lr, [r12, #14*4]
	// powrot do trybu przerwan sprzetowych
	msr cpsr_cxsf, r11
	// zaladowanie pozostalych rejestrow
	ldmia r12, {r0-r11}
	str r11, [sp, #-4]!
	ldr r11, [r12, #12*4]
	str r11, [sp, #-4]!
	ldr lr, [r12, #15*4]
	ldr r11, [r12, #16*4]
	// taka sztuczka: jesli do spsr zaladujemy poprzedni stan
	// cpsr dla tego zadania to po zakonczeniu przelaczania
	// wyladuje on w cpsr
	msr spsr_cxsf, r11
	// i ustawienie timer0 dla tego zadania
	ldr r12, =VIC_BASE
	ldr r11, =0
	str r11, [r12, #VectAddr_OFS]
	ldr r12, =T0_BASE
	ldr r11, =1
	str r11, [r12, #IR_OFS]
	str r11, [r12, #TCR_OFS]
	ldr r11, =-3
	str r11, [r12, #TC_OFS]
	ldr r12, [sp], #4
	ldr r11, [sp], #4
	// skok do zadania
	movs pc, lr
	;#####KOD UNIWERSALNY!!! NIE KASOWAC!!!#####

}

__asm void zadanie_dodaj(const unsigned char p, const unsigned int s, void (*f)(void) , const char* n){
	stmfd sp!, {r9, r10, r11, r12, lr}
	ldr r12, =OSTATNIE_ZADANIE
	ldr r11, [r12, #4]
	cmp r11, #0
	bne zd_koniec
	mov r11, r0
	add r1, r1, #(4+4+17*4+DLUGOSC_NAZWY)
	mov r0, r1
	IMPORT r_start
	bl r_start
	cmp r0, #0
	beq zd_koniec
			   // zapisanie priorytetu
			   str r11, [r0]
			   mov r11, #0
			   str r11, [r0, #4]
			   // wyznaczenie wierzcholka stosu
			   add r1, r0, r1
			   // zapisanie wiercholka stosu
			   str r1, [r0, #(4+4+13*4)]
			   // adrs funkcji jak pierwsza i ostatnie instrukcja
			   // zapobieganie ucieczce z zadania
			   str r2, [r0, #(4+4+14*4)]
			   str r2, [r0, #(4+4+15*4)]
			   // poczatkowy stan procesora, bo jesli zostawimy zero to lr i sp nie zostana przywrocone
			   ldr r11, =0x00000010
			   str r11, [r0, #(4+4+16*4)]
			   // zapisanie nazwy zadania
			   add r11, r0, #(4+4+17*4)
			   ldr r10, =DLUGOSC_NAZWY
dodaj_nazwe subs r10, r10, #1
			bmi ograniczenie_dlugosci
			ldrb r9, [r3], #1
			strb r9, [r11], #1
			cmp r9, #0
			bne dodaj_nazwe
			// koniec nazwy lub nazwa zbyt dluga
			// nowe zadanie jako
ograniczenie_dlugosci ldr r10, [r12]
					  ldr r9, =LISTA_ZADAN
					  subs r9, r10, r9
					  //  pierwsze jesli nie dodano wczesniej
					  ldrmi r10, =LISTA_ZADAN
					  // lub ostatnie jesli istnialo jakies oprocz bezczynnosci
dalej str r0, [r10, #4]!
	  str r10, [r12]
zd_koniec ldmfd sp!, {r9, r10, r11, r12, lr}
		  bx lr
}

__asm void zadanie_usun(const unsigned int n, const unsigned int s){
	stmfd sp!, {r9, r10, r11, r12, lr}
	ldr r12, =LISTA_ZADAN
	mov r11, #4
	mla r0, r11, r0, r12
	ldr r11, =OSTATNIE_ZADANIE
	ldr r10, [r11]
	cmp r0, r12
	blt koniec_usuwania
	cmp r0, r10
	bgt koniec_usuwania
	mov r12, r0
	ldr r0, [r0]
usun_zadanie cmp r12, r10
			 // scalamy liste aby usunac puste pole po zadaniu
			 ldrlt r9, [r12, #4]
			 strlt r9, [r12], #4
			 blt usun_zadanie
			 // ostatnie jest teraz o jeden wczesniej
ostatnie_zadanie sub r12, r12, #4
				 str r12, [r11]
				 add r1, r1, #(4+4+17*4+DLUGOSC_NAZWY)
				 IMPORT z_start
				 bl z_start
koniec_usuwania ldmfd sp!, {r9, r10, r11, r12, lr}
				bx lr
}

/*
__asm void zadanie_uspij(const unsigned int n){
	stmfd sp!, {r8-r12}
	// czy nr zadania miesci sie w indeksie
	cmp r0, #ILOSC_ZADAN
	bgt zu_koniec
	cmp r0, #0
	ble zu_koniec
	ldr r12, =ZRZUT_SRODOWISKA
	ldr r11, =4+4+17*4+DLUGOSC_NAZWY+WIELKOSC_STOSU
	mla r12, r11, r0, r12
	ldr r11, =LISTA_ZADAN
	ldr r9, =OSTATNIE_ZADANIE
	ldr r8, [r9]
	// czy zadanie bylo uruchomione
zu_szukaj add r11, r11, #4
		  cmp r8, r11
		  // jesli nie to koniec
		  blt zu_koniec
		  ldr r10, [r11]
		  cmp r10, r12
		  bne zu_szukaj
		  // usuwamy z listy wykonywanych
zu_usun cmp r8, r11
		beq zu_ostatnie
		// zsuamy liste
		ldr r10, [r11, #4]
		str r10, [r11], #4
		b zu_usun
		// ostatnie o jeden wczesniej
zu_ostatnie str r11, [r9]
zu_koniec ldmfd sp!, {r8-r12}
		  bx lr
}

__asm void zadanie_obudz(const unsigned int n){
	stmfd sp!, {r8-r12}
	// czy nr miesci sie w granicach
	cmp r0, #ILOSC_ZADAN
	bgt zo_koniec
	cmp r0, #1
	blt zo_koniec
	ldr r12, =ZRZUT_SRODOWISKA
	ldr r11, =4+4+17*4+DLUGOSC_NAZWY+WIELKOSC_STOSU
	mla r12, r11, r0, r12
	ldr r11, =LISTA_ZADAN
	ldr r9, =OSTATNIE_ZADANIE
	ldr r8, [r9]
	// szukamy zadania o takim numrze
zo_szukaj add r11, r11, #4
		  ldr r10, [r11]
		  cmp r10, r12
		  // jesli juz uruchomione to koniec
		  beq zo_koniec
		  cmp r8, r11
		  bge zo_szukaj
		  // dodajemy do listy jako ostatnie
		  str r12, [r8, #4]!
		  str r8, [r9]
zo_koniec ldmfd sp!, {r8-r12}
		  bx lr
}
*/

// na razie nie istotne pozniej bedzie to przygotowanie sprzetu
__asm void sprzet_ustaw(void){
	ldr r0, =12000000
	swi 5
	;
	;
	;
	bx lr
}

// proste funkcje opozniajace, mam to jeszcze ulepszyc pozniej
__asm void czekaj_us(const unsigned int c){
	stmfd sp!, {r9, r10, r11, r12}
czekaj ldr r12, =CZAS_PLANISTY
	   ldr r11, [r12]
	   add r10, r11, r0
	   ldr r9, =T0_BASE
czekaj_petla ldr r11, [r12]
			 cmp r11, r10
			 bge cu_koniec
			 ;ldr r12, =T0_BASE
			 ;ldr r11, [r12, #TC_OFS]
			 ldr r11, [r9, #TC_OFS] //
			 add r11, r11, #1 ; hhhhhhhhhhhhhhhhhhh 1
			 ;str r11, [r12, #MR0_OFS]
			 str r11, [r9, #MR0_OFS] //
			 b czekaj_petla
cu_koniec ldmfd sp!, {r9, r10, r11, r12}
			 bx lr
}

__asm void czekaj_ms(const unsigned int c){
	stmfd sp!, {r9, r10, r11, r12}
	ldr r12, =12000
	mov r11, r0
	mul r0, r11, r12
	b czekaj
}

__asm void czekaj_s(const unsigned int c){
	stmfd sp!, {r9, r10, r11, r12}
	ldr r12, =12000000
	mov r11, r0
	mul r0, r11, r12
	b czekaj
}
