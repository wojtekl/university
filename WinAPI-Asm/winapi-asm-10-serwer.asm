format pe console
entry start

include 'win32w.inc'

STD_OUTPUT_HANDLE EQU -11

section '.data' data readable writeable
  konsolaUchwytWyjscie dd 0
  konsolaLiczbaWypisanych dd 0
  bufor: times 100 du 0, 10 ,13, 0
  gniazdo1 dd ?
  gniazdo2 dd ?
  rozmiar dd ?
  wSAData WSADATA
  sockaddr_in1 sockaddr_in
  sockaddr_in2 sockaddr_in
  komunikatPotwierdzenie du 'Klient po³¹czy³ siê.', 10,\
    13, 0
  komunikatWyslany du 'Wysy³am komunikat do klienta.', 10,\
    13, 0
  komunikatSerwera db 'Komunikat serwera.', 10, 13, 0
  komunikatBlad du 'B³¹d serwera!', 10, 13, 0

section '.text' code readable executable
start:
  stdcall [GetStdHandle], STD_OUTPUT_HANDLE
  mov [konsolaUchwytWyjscie], eax
  stdcall [WSAStartup], 202h, wSAData
  cmp eax, 0
  jnz blad
  stdcall [socket], 2, 1, 0
  mov [gniazdo1], eax
  cmp eax, not 0
  jz blad
  mov [sockaddr_in1.sin_family], 2
  mov [sockaddr_in1.sin_addr], 0
  mov [sockaddr_in1.sin_port], 2000
  stdcall [bind], [gniazdo1], sockaddr_in1,\
    sizeof.sockaddr_in
  cmp eax, 0
  jne blad
  stdcall [listen], [gniazdo1], 2
  cmp eax, 0
  jnz blad
  mov [rozmiar], sizeof.sockaddr_in
  stdcall [accept], [gniazdo1], [sockaddr_in2], rozmiar
  mov [gniazdo2], eax
  stdcall [CharToOemW], komunikatPotwierdzenie,\
    komunikatPotwierdzenie
  stdcall [lstrlenW], komunikatPotwierdzenie
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie],\
    komunikatPotwierdzenie, eax, konsolaLiczbaWypisanych, 0
  stdcall [recv], [gniazdo2], bufor, 100, 0
  stdcall [lstrlenA], bufor
  stdcall [WriteConsoleA], [konsolaUchwytWyjscie],\
    bufor, eax, konsolaLiczbaWypisanych, 0
  stdcall [CharToOemW], komunikatWyslany, komunikatWyslany
  stdcall [lstrlenW], komunikatWyslany
  stdcall [WriteConsoleW], [konsolaUchwytWyjscie],\
    komunikatWyslany, eax, konsolaLiczbaWypisanych, 0
  stdcall [lstrlenA], komunikatSerwera, 0
  stdcall [send], [gniazdo2], komunikatSerwera, eax
  stdcall [shutdown], [gniazdo2], 0
  stdcall [closesocket], [gniazdo2]
czekaj:
  jmp czekaj
  stdcall [closesocket], [gniazdo1]
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

section '.idata' import data readable writeable
  library\
    kernel32, 'kernel32.dll',\
    user32, 'user32.dll',\
    wsock32, 'wsock32.dll'

  include 'api\kernel32.inc'
  include 'api\user32.inc'
  include 'api\wsock32.inc'