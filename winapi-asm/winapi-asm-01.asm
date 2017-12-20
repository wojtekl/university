format pe console
entry start

include 'win32a.inc'

STD_OUTPUT_HANDLE EQU -11
FILE_ATTRIBUTE_ARCHIVE EQU 20h
OPEN_ALWAYS EQU 4
GENERIC_WRITE EQU 40000000h
INVALID_HANDLE_VALUE EQU -1

section '.bss' data readable writeable
  konsola_uchwyt dd ?
  argc dd ?
  argv dd ?
  konsola_liczba_wypisanych dd ?
  plik_uchwyt dd ?
  plik_liczba_zapisanych dd ?
  niepoprawna_liczba_argumentow db "Niepoprawna liczba argumentów!", 10, 13, 0
  n_u_sie_otworzyc_pliku db "Nie uda³o siê stworzyæ pliku!", 10, 13, 0

section '.text' code readable executable
start:
  call [AllocConsole]
  push STD_OUTPUT_HANDLE
  call [GetStdHandle]
  mov [konsola_uchwyt], eax
  call [GetCommandLineW]
  push argc
  push eax
  call [CommandLineToArgvW]
  mov [argv], eax
  mov ebx, 3
  cmp [argc], ebx
  jne argumenty_niepoprawne

  mov edi, [argv]
  add edi, 4
  mov eax, [edi]

  push 0
  push FILE_ATTRIBUTE_ARCHIVE
  push OPEN_ALWAYS
  push 0
  push 0
  push GENERIC_WRITE
  push eax
  call [CreateFileW]

  mov [plik_uchwyt], eax
  cmp eax, INVALID_HANDLE_VALUE
  jne plik_otwarty

  push n_u_sie_otworzyc_pliku
  push n_u_sie_otworzyc_pliku
  call [CharToOemA]

  mov ebx, n_u_sie_otworzyc_pliku
  push ebx
  call [lstrlenA]

  push 0
  push konsola_liczba_wypisanych
  push eax
  push ebx
  push [konsola_uchwyt]
  call [WriteConsoleA]
  jmp koniec

plik_otwarty:
  mov edi, [argv]
  add edi, 8
  mov ebx, [edi]
  push ebx
  call [lstrlenW]
  xor esi, esi
  mov esi, 2
  mul esi
  push 0
  push plik_liczba_zapisanych
 push eax
  push ebx
  push [plik_uchwyt]
  call [WriteFile]
  jmp koniec

argumenty_niepoprawne:
  push niepoprawna_liczba_argumentow
  push niepoprawna_liczba_argumentow
  call [CharToOemA]
  mov ebx, niepoprawna_liczba_argumentow
  push ebx
  call [lstrlenA]
  push 0
  push konsola_liczba_wypisanych
  push eax
  push ebx
  push [konsola_uchwyt]
  call [WriteConsoleA]

koniec:
  call [FreeConsole]
  push 0
  call [ExitProcess]
  ret

section '.idata' import data readable writeable
library\
  kernel, 'kernel32.dll',\
  shell, 'shell32.dll',\
  user, 'user32.dll'

import kernel,\
  ExitProcess, 'ExitProcess',\
  AllocConsole, 'AllocConsole',\
  GetStdHandle, 'GetStdHandle',\
  GetCommandLineW, 'GetCommandLineW',\
  lstrlenA, 'lstrlenA',\
  WriteConsoleA, 'WriteConsoleA',\
  FreeConsole, 'FreeConsole',\
  CreateFileW, 'CreateFileW',\
  lstrlenW, 'lstrlenW',\
  WriteFile, 'WriteFile'

import shell,\
  CommandLineToArgvW, 'CommandLineToArgvW'

import user,\
  CharToOemA, 'CharToOemA'
