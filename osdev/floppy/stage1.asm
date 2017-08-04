use16
org 7c00h

P13H_SEKTOR_ODCZYTAJ equ 02h

jmp dword 0000h:start

start:
  mov ax, 0b800h
  mov es, ax
  xor di, di
  mov word [es:di], 4141h
  
  mov ax, 2000h
  mov es, ax
  xor bx, bx
  mov ah, P13H_SEKTOR_ODCZYTAJ
  mov al, 0cch
  nop
  nop
  mov ch, 0h
  mov cl, 2h
  mov dh, 0h
  int 13h
  
  jmp dword 2000h:0000h

if ($ - $$) > 512
  "Za duzy plik!"
end if
times 510 - ($ - $$) db 0h

db 55h, 0aah

