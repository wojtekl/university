#include <lpc210x.h>
#include "kernel/zadania.h"
#include "kernel/semafor.h"
//#include "uart/uart.h"
#include "kernel/magazyn.h"

semafor s1;

void innafunkcja(int a){
	if((IOPIN&a)!=a) IOSET|=a;
	else IOCLR|=a;
	a=a+0;
	a=a*1;
	semafor_wez(s1, 1000);
	a=a-1;
	a=a/1;}

__asm void funasm(void){
	;
	bx lr
}

void funkcja(void){
	IOSET|=0x0000f000;
	for(;;){
	innafunkcja(0x00000f00);
	funasm();
	czekaj_ms(10);
	}
}

void licznik(void){
	int l=0;
	IOSET|=0x0000f000;
	
	semafor_daj(s1);
	for(;;) ++l;
}

int main(){
	IODIR|=0x0000ffff;
	
	//sprzet_ustaw();
	//uart_wlacz();
	s1=semafor_stworz();
	zadanie_dodaj(1, 16, funkcja, "nowy");
	zadanie_dodaj(2, 32, funkcja, "drugi");
	zadanie_usun(1, 16);
	//zadanie_uspij(2);
	zadanie_dodaj(5, 8, licznik, "trzeci");
	//zadanie_obudz(2);
	//zadanie_dodaj(1, 0, licznik, "licznik");
	planista_uruchom();
	IOSET|=0x0000000f;
	/*__asm{
		swi 15;
		swi 13;
	}*/
//return 0;
}
