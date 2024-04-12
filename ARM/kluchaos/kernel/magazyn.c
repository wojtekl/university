#define RAM_ZA_KONIEC		0x40010000
#define	MAGAZYN_WIELKOSC	0x8000
#define WIELKOSC_WYROWNANA	(((MAGAZYN_WIELKOSC+3)/4)*4)
#define MAGAZYN_SKLAD		RAM_ZA_KONIEC-WIELKOSC_WYROWNANA
#define MAPA_WIELKOSC		WIELKOSC_WYROWNANA/32
#define	MAGAZYN_MAPA		MAGAZYN_SKLAD-MAPA_WIELKOSC

__asm void* rezerwuj(const unsigned int w){
r_start stmfd sp!, {r6-r12}
	EXPORT r_start
	mov r12, #0
	ldr r11, =MAGAZYN_MAPA-4
	mov r10, #1
	sub r0, r0, #1
	lsr r0, #2
	mov r9, r0
	ldr r8, =WIELKOSC_WYROWNANA
szukaj ldr r7, [r11, #4]!
dalej tst r7, r10
	  movne r9, r0
	  bne r_pomin
	  subseq r9, r9, #1
	  bmi adres
r_pomin add r12, r12, #1
	  cmp r12, r8
	  movge r0, #0
	  bge koniec
	  lsls r10, #1
	  moveq r10, #1
	  beq szukaj
	  b dalej
adres sub r12, r12, r0
	  mov r10, r12, lsr #5
	  mvn r11, #0x00000001f
	  bic r11, r12, r11
	  ldr r9, =MAGAZYN_MAPA
	  add r10, r9, r10, lsl #2
	  mov r8, #1
	  lsl r8, r11
	  mov r7, r0
oznacz ldr r6, [r10]
	   orr r6, r6, r8
	   str r6, [r10]
	   lsls r8, #1
	   moveq r8, #1
	   addeq r10, r10, #4
	   subs r7, r7, #1
	   bpl oznacz
	   ldr r11, =MAGAZYN_SKLAD
	   mov r10, #4
	   mla r0, r10, r12, r11
	   ldmfd sp!, {r6-r12}
koniec bx lr}

__asm void* zwolnij(void* a, const unsigned int w){
z_start stmfd sp!, {r9-r12}
	EXPORT z_start
	ldr r12, =MAGAZYN_SKLAD
	sub r12, r0, r12
	lsr r12, #2
	lsr r1, #2
	mvn r11, #0x0000001f
	bic r11, r12, r11
	mov r12, r12, lsr #5
	ldr r10, =MAGAZYN_MAPA
	add r12, r10, r12
	ldr r10, [r12]
	mov r9, #1
	mov r11, r9, lsl r11
	bic r10, r10, r11
	str r10, [r12]
	sub r1, r1, #2
	ldr r9, =RAM_ZA_KONIEC
z_dalej lsls r11, #1
	moveq r11, #1
	addeq r12, r12, #4
	cmp r12, r9
	bge z_koniec
	ldr r10, [r12]
	bic r10, r10, r11
	str r10, [r12]
	subs r1, r1, #1
	bpl z_dalej
	movmi r0, #0
	ldmfd sp!, {r9-r12}
z_koniec bx lr}
