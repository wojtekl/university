format pe dll
entry start

include 'win32w.inc'

PLIK_NAZWA EQU 'winapi-asm-11-dll.dll'

section '.data' data readable writeable
tekst du 'Biblioteka za³adowana.', 0

section '.text' code readable executable
start:
  mov eax, dword [ebp + 0ch]
  cmp eax, DLL_PROCESS_DETACH
  je koniec
  cmp eax, DLL_PROCESS_ATTACH
  je koniec
koniec:
  mov eax, 1
  ret

proc funkcja parametr
  stdcall [SendMessageW], [parametr], WM_SETTEXT, 100,\
    tekst
  ret
endp

section '.idata' data readable writeable import
library\
  user32, 'user32.dll'

include 'api\user32.inc'

section '.edata' data readable export
export PLIK_NAZWA,\
  funkcja, 'funkcja'

data fixups
end data
