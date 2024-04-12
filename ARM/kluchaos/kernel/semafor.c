#include "../kernel/magazyn.h"
#define	T0_BASE				0xE0004000
#define	TC_OFS				0x08
#define	MR0_OFS				0x18
#define CZAS_PLANISTY		0x40001000

typedef unsigned char* semafor;

__asm semafor semafor_stworz(){
	stmfd sp!, {r12, lr}
	mov r0, #4
	IMPORT r_start
	bl r_start
	mvn r12, #0
	str r12, [r0]
	ldmfd sp!, {r12, lr}
	bx lr
}

__asm void semafor_wez(semafor s, const unsigned int c){
	stmfd sp!, {r9-r12}
	ldr r12, =T0_BASE
	ldr r11, =CZAS_PLANISTY;[r12, #TC_OFS]
	ldr r10, [r11]
	add r1, r10, r1
sw_poczatek ldr r10, [r0]
			cmp r10, #0
			beq wyjdz
			ldrne r10, [r11];[r12, #TC_OFS]
			ldrne r9, [r12, #TC_OFS]
			addne r10, r9, r10
			cmp r10, r1
			bge wyjdz
			addne r9, r9, #2
			strne r9, [r12, #MR0_OFS]
			nop
			bne sw_poczatek
wyjdz mvn r12, #0
	  str r12, [r0]
	  ldmfd sp!, {r9-r12}
	  bx lr
}

__asm void semafor_daj(semafor s){
	str r12, [sp, #-4]!
	mov r12, #0
	str r12, [r0]
	ldr r12, [sp], #4
	bx lr
}
