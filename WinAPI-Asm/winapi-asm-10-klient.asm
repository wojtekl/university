format pe console
entry start

include 'win32w.inc'

STD_OUTPUT_HANDLE EQU -11

section '.data' data readable writeable
  konsolaUchwytWyjscie dd 0
  konsolaLiczbaWypisanych dd 0
  bufor1: times 100 du 0, 10, 13, 0
  bufor2: times 100 du 0, 10, 13 ,0
  gniazdo dd ?
  rozmiar dd ?
  wSAData WSADATA
  sockaddrIn sockaddr_in
  nazwa db 'localhost', 0
  adres dd ?
  komunikatAdres du 'Adres IP serwera: %hu.%hu.%hu.%hu.',\
  10, 13, 0
  komunikatUruchomienie du 'Klient uruchomiony!', 10, 13, 0
  komunikatKlienta db 'Komunikat klienta.', 10, 13, 0
  komunikatOdbieranie du 'Odbieram komunikat od serwera:',\
    10, 13, 0
  komunikatBlad du 'B³¹d klienta!', 10, 13, 0

section '.text' code readable executable
start:
  stdcall [GetStdHandle], STD_OUTPUT_HANDLE
  mov [konsolaUchwytWyjscie], eax
  stdcall [WSAStartup], 0202h, wSAData
  cmp eax, 0
  jnz blad
  stdcall [gethostbyname], nazwa
  cmp eax, 0
  jz blad
  mov ebx, [eax + 12]
  mov edx, [ebx]
  mov edx, [edx]
  mov [adres], edx
  shr edx, 24
  and edx, 000000ffh
  push edx
  mov edx, adres
  shr edx, 16
  and edx, 000000ffh
  push edx
  mov edx, adres
  shr edx, 8
  and edx, 000000ffh
  push edx
  mov edx, adres
  and edx, 000000ffh
  push edx
  stdcall [wsprintfW], bufor2, komunikatAdres
  mov ebx, bufor2
  stdcall [lstrlenW], ebx
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie], ebx,\
    eax, konsolaLiczbaWypisanych, 0
  mov edx, [adres]
  mov [sockaddrIn.sin_family], 2
  mov [sockaddrIn.sin_addr], edx
  mov [sockaddrIn.sin_port], 2000
  stdcall [socket], 2, 1, 0
  mov [gniazdo], eax
  stdcall [connect], [gniazdo], sockaddrIn,\
    sizeof.sockaddr_in
  cmp eax, 0
  jnz blad
  stdcall [lstrlenW], komunikatUruchomienie
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie],\
    komunikatUruchomienie, eax, konsolaLiczbaWypisanych, 0
  stdcall [lstrlenA], komunikatKlienta, 0
  stdcall [send], [gniazdo], komunikatKlienta, eax
  stdcall [CharToOemW], komunikatOdbieranie,\
    komunikatOdbieranie
  stdcall [lstrlenW], komunikatOdbieranie
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie],\
    komunikatOdbieranie, eax, konsolaLiczbaWypisanych, 0
  stdcall [recv], [gniazdo], bufor1, 100, 0
  stdcall [lstrlenA], bufor1
  stdcall [WriteConsoleA], [konsolaUchwytWyjscie], bufor1,\
    eax, konsolaLiczbaWypisanych, 0
  stdcall [closesocket], [gniazdo]
czekaj:
  jmp czekaj
koniec:
  stdcall [ExitProcess], 0
blad:
  jmp blad_komunikat
  jmp koniec

proc blad_komunikat
  stdcall [lstrlenW], komunikatBlad
  push eax
  stdcall [CharToOemW], komunikatBlad, komunikatBlad
  pop eax
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie],\
    komunikatBlad, eax, konsolaLiczbaWypisanych, 0
  ret
endp

section '.idata' data readable writeable import
  library\
    kernel32, 'kernel32.dll',\
    user32, 'user32.dll',\
    wsock32, 'wsock32.dll'

  include 'api\kernel32.inc'
  include 'api\user32.inc'
  include 'api\wsock32.inc'
