use16
org 0000h

EKRAN_TEKST_ADRES_16BIT equ 0b800h
EKRAN_TEKST_ADRES_32BIT equ 0b8000h

CR0_32BIT equ 1h
CR0_PAGING equ 1h shl 31
CR4_PAE equ 1h shl 5
IA32_EFER_MSR_ADRES equ 0c0000080h
IA32_EFER_MSR_LONG_MODE equ 1h shl 8

start:
  mov ax, 2000h
  mov ds, ax
  mov es, ax
  mov ax, 1f00h
  mov ss, ax
  xor sp, sp
  
  mov ax, EKRAN_TEKST_ADRES_16BIT
  mov fs, ax
  mov bx, 0h
  mov ax, 4141h
  mov [fs:bx], ax
  
  lgdt [GDT_addr]
  mov eax, cr0
  or eax, CR0_32BIT
  mov cr0, eax
  jmp fword 00000008h:(00020000h + start32)
  
start32:
use32
  mov ax, 10h
  mov ds, ax
  mov es, ax
  mov ss, ax
  
  lea eax, [EKRAN_TEKST_ADRES_32BIT]
  mov dword [eax], 41414141h
  
  mov eax, (PML4 - $$) + 20000h
  mov cr3, eax
  
  mov eax, cr4
  or eax, CR4_PAE
  mov cr4, eax
  
  mov ecx, IA32_EFER_MSR_ADRES
  rdmsr
  or eax, IA32_EFER_MSR_LONG_MODE
  wrmsr
  
; wlaczenie pagingu
  mov eax, cr0
  or eax, CR0_PAGING
  mov cr0, eax
  
  lgdt [GDT64_addr + 20000h]
  jmp fword 00000008h:(00020000h + start64)
  
start64:
use64
  mov ax, 10h
  mov ds, ax
  mov es, ax
  mov ss, ax
  
  mov rax, EKRAN_TEKST_ADRES_32BIT
  mov rdx, 4242424242424242h
  mov [rax], rdx
  
; wczytanie elfa
loader:
  mov rsi, [dword 20000h + kernel + 20h]
  add rsi, 20000h + kernel
  movzx ecx, word [dword 20000h + kernel + 38h]
  cld
  xor r14, r14
ph_loop:
  mov eax, [rsi + 0h]
  cmp eax, 1h
  jne next
  mov r8, [rsi + 8h]
  mov r9, [rsi + 10h]
  mov r10, [rsi + 20h]
  test r14, r14
  jnz skip
  mov r14, r9
skip:
  mov rbp, rsi
  mov r15, rcx
  lea rsi, [dword 20000h + kernel + r8d]
  mov rdi, r9
  mov rcx, r10
  rep movsb
  mov rcx, r15
  mov rsi, rbp
next:
  add rsi, 20h
  loop ph_loop
    
  mov rsp, 30f000h
  mov rdi, r14
  mov rax, [dword 20000h + kernel + 18h]
  call rax
  
jmp $

GDT_addr:
  dw (GDT_end - GDT) - 1
  dd 20000h + GDT

times (32 - ($ - $$) mod 32) db 0cch

GDT:
  dd 0h, 0h
  
  dd 0ffffh
  dd (0ah shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 22) or (1 shl 23)
  
  dd 0ffffh
  dd (2h shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 22) or (1 shl 23)
  
  dd 0, 0
  
GDT_end:

GDT64_addr:
  dw (GDT64_end - GDT64) - 1
  dd 20000h + GDT64

times (32 - ($ - $$) mod 32) db 0cch

GDT64:
  dd 0h, 0h
  
  dd 0ffffh
  dd (0ah shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 21) or (1 shl 23)
  
  dd 0ffffh
  dd (2h shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 21) or (1 shl 23)
  
  dd 0, 0
  
GDT64_end:

times (4096 - ($ - $$) mod 4096) db 0h

PML4:
  dq 1 or (1 shl 1) or (PDPTE - $$ + 20000h)
  times 511 dq 0h
  
PDPTE:
  dq 1 or (1 shl 1) or (1 shl 7)
  times 511 dq 0h
  
times (512 - ($ - $$) mod 512) db 0h

kernel:

