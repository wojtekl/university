format mz
entry main:start

P21H_PRZERWANIE_PROCEDURA_POBIERZ equ 3500h
P21H_PRZERWANIE_PROCEDURA_PODMIEN equ 2500h
P21H_ZAMKNIJ equ 4ch
P21H_TEKST_$_WYPISZ equ 9h

segment main

start:
  mov cx, cs
  mov ds, cx
  mov bx, podmiana
  mov al, 0bah
  mov byte [cs:bx], al
podmiana:
db 0ah, napis, 0h
  mov ah, P21H_TEKST_$_WYPISZ
  int 21h
  
  mov ax, (P21H_PRZERWANIE_PROCEDURA_POBIERZ or 21h)
  int 21h
  mov ax, (P21H_PRZERWANIE_PROCEDURA_PODMIEN or 03h)
  mov dx, bx
  push es
  pop ds
  int 21h

  mov cx, cs
  mov ds, cx
  mov dx, napis2
  mov ah, P21H_TEKST_$_WYPISZ
  int 3h

  mov ah, P21H_ZAMKNIJ
  int 3h
  
napis db 'Nie zgadniesz$'
napis2 db 'Nie zdebugujesz$'
  
stack 100h
