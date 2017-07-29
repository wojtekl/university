use16
org 0000h

start:
  mov ax, 2000h
  mov ds, ax
  mov es, ax
  mov ax, 1f00h
  mov ss, ax
  xor sp, sp
  
  mov ax, 0b800h
  mov fs, ax
  mov bx, 0h
  mov ax, 4141h
  mov [fs:bx], ax
  
  lgdt [GDT_addr]
  mov eax, cr0
  or eax, 1
  mov cr0, eax
  
use32

  jmp far 00000008h:(00020000h + start32)
  
start32:
  mov ax, 10h
  mov ds, ax
  mov es, ax
  mov ss, ax
  
  lea eax, [0b800h]
  mov dword [eax], 41414141h
  
jmp $

GDT_addr:
  dw (GDT_end - GDT) - 1
  dd 20000h + GDT

times 32 - ($ - $$) mod 32 db 0cch
GDT:
  dd 0h, 0h
  
  dd 0ffffh
  dd (10h shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 22) or (1 shl 23)
  
  dd 0ffffh
  dd (2h shl 8) or (1 shl 12) or (1 shl 15) or (0fh shl 16) or (1 shl 22) or (1 shl 23)
  
  dd 0, 0
  
GDT_end:

times 1337 db 41h

