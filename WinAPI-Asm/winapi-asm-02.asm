format pe console
entry start

include 'win32a.inc'

STD_OUTPUT_HANDLE EQU -11
STD_INPUT_HANDLE EQU -10
KOLOR EQU 1h + 8h

section '.bss' data readable writeable
konsola_uchwyt_wyjscie dd ?
konsola_uchwyt_wejscie dd ?
konsola_liczba_wypisanych dd ?
bufor: times 200 db 0
wprowadz_tekst db "Wprowadü tekst:", 10, 13, 0

section '.text' code readable executable
start:
  call [AllocConsole]
  push STD_OUTPUT_HANDLE
  call [GetStdHandle]
  mov [konsola_uchwyt_wyjscie], eax
  push STD_INPUT_HANDLE
  call [GetStdHandle]
  mov [konsola_uchwyt_wejscie], eax
  push wprowadz_tekst
  push wprowadz_tekst
  call [CharToOemA]
  push wprowadz_tekst
  call [lstrlenA]
  push 0
  push konsola_liczba_wypisanych
  push eax
  push wprowadz_tekst
  push [konsola_uchwyt_wyjscie]
  call [WriteConsoleA]
  push 0
  push konsola_liczba_wypisanych
  push 200
  push bufor
  push [konsola_uchwyt_wejscie]
  call [ReadConsoleA]
  push KOLOR
  push [konsola_uchwyt_wyjscie]
  call [SetConsoleTextAttribute]
  push bufor
  call [lstrlenA]
  push 0
  push konsola_liczba_wypisanych
  push eax
  push bufor
  push [konsola_uchwyt_wyjscie]
  call [WriteConsoleA]
  mov eax, 5fffffffh
opoznienie:
  loop opoznienie
  call [FreeConsole]
  push 0
  call [ExitProcess]
  ret

section '.idata' import data readable writeable
library\
  kernel, 'kernel32.dll',\
  user, 'user32.dll'

import kernel,\
  AllocConsole, 'AllocConsole',\
  GetStdHandle, 'GetStdHandle',\
  lstrlenA, 'lstrlenA',\
  WriteConsoleA, 'WriteConsoleA',\
  ReadConsoleA, 'ReadConsoleA',\
  SetConsoleTextAttribute, 'SetConsoleTextAttribute',\
  FreeConsole, 'FreeConsole',\
  ExitProcess, 'ExitProcess'

import user,\
  CharToOemA, 'CharToOemA'
