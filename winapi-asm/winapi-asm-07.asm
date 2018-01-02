format pe gui
entry start

include 'win32a.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or BS_DEFPUSHBUTTON
STYLE_EDITBOX EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or WS_BORDER
STYLE_STATIC EQU WS_CHILD or WS_VISIBLE

WINDOW_NAME EQU 'Windows App'
BUTTON_NAME EQU 'Wynik'
BUTTON_CLASSNAME EQU 'BUTTON'
EDITBOX_NAME EQU ''
EDITBOX_CLASSNAME EQU 'EDIT'
STATIC1_NAME EQU '='
STATIC2_NAME EQU '+'
STATIC_CLASSNAME EQU 'STATIC'

IDC_BUTTON EQU 101
IDC_EDITBOX1 EQU 102
IDC_EDITBOX2 EQU 103
IDC_EDITBOX3 EQU 104
IDC_DIALOG EQU 105

section '.data' data readable writeable
  hThisInstance dd 0
  bufor1: times 100 db 0
  bufor2: times 100 db 0
  dlugosc dd 0
  liczba dd 0

section '.text' code readable executable
start:
  push 0
  call [GetModuleHandleA]
  mov [hThisInstance], eax
  push 0
  push WindowProcedure
  push 0
  push IDC_DIALOG
  push eax
  call [DialogBoxParamA]
  push 0
  call [ExitProcess]

proc WindowProcedure
  push ebp
  mov ebp, esp
  push ebx
  push esi
  push edi
  cmp dword [ebp + 0ch], WM_COMMAND
  je wm_command
  cmp dword [ebp + 0ch], WM_CLOSE
  je wm_close
  jmp return
wm_command:
  cmp dword [ebp + 10h], IDC_BUTTON
  jne dalej
  push bufor1
  push 100
  push WM_GETTEXT
  push IDC_EDITBOX1
  push dword [ebp + 8h]
  call [SendDlgItemMessageA]
  push bufor1
  call [lstrlenA]
  mov [dlugosc], eax
  xor eax, eax
  call atoi
  call czysc
  push bufor1
  push 100
  push WM_GETTEXT
  push IDC_EDITBOX2
  push dword [ebp + 8h]
  call [SendDlgItemMessageA]
  push bufor1
  call [lstrlenA]
  mov [dlugosc], eax
  xor eax, eax
  call atoi
  call czysc
  mov eax, [liczba]
  call itoa
  push bufor2
  push 100
  push WM_SETTEXT
  push IDC_EDITBOX3
  push dword [ebp + 8h]
  call [SendDlgItemMessageA]
  call czysc
  mov [liczba], 0
dalej:
  jmp return
wm_close:
  push 0
  push dword [ebp + 8h]
  call [EndDialog]
return:
  xor eax, eax
  pop edi
  pop esi
  pop ebx
  pop ebp
  ret
endp

proc czysc
  pushad
  mov ecx, 100
  mov [dlugosc], 0
  mov byte [bufor1], 0
  mov byte [bufor2], 0
.for:
  mov byte [bufor1 + ecx], 0
  mov byte [bufor2 + ecx], 0
  loop czysc.for
  popad
  ret
endp

proc atoi
  pushad
  mov ecx, [dlugosc]
  mov ebx, 10
  mov esi, ecx
  mov edi, 0
  cmp esi, 0
  je atoi.koniec
  dec esi
.cyfra:
  mov eax, [bufor1 + esi]
  and eax, 000000ffh
  sub eax, 30h
  push ecx
  cmp edi, 0
  je atoi.jednosci
  mov ecx, edi
.pozycja:
  mul ebx
  loop atoi.pozycja
.jednosci:
  inc edi
  pop ecx
  add [liczba], eax
  jc atoi.koniec
  xor eax, eax
  dec esi
  loop atoi.cyfra
.koniec:
  popad
  ret
endp

proc itoa
  pushad
  xor esi, esi
  xor ebx, ebx
  xor edx, edx
  mov ebx, 10
.cyfra:
  div ebx
  push edx
  xor edx, edx
  inc esi
  cmp eax, 0
  jne itoa.cyfra
  mov ecx, esi
  xor esi, esi
.zapisz:
  pop edx
  add edx, 30h
  mov dword [bufor2 + esi], edx
  inc esi
  loop itoa.zapisz
  popad
  ret
endp

section '.rsrc' data readable resource
  directory RT_DIALOG, dialogs
  resource dialogs, IDC_DIALOG,\
    LANG_ENGLISH + SUBLANG_DEFAULT, szClassName
  dialog szClassName, WINDOW_NAME, 10, 10, 249, 166,\
    WS_OVERLAPPEDWINDOW, 0, 0, 'MS Sans Serif', 8
    dialogitem BUTTON_CLASSNAME, BUTTON_NAME, IDC_BUTTON,\
      183, 69, 54, 15, STYLE_BUTTON, 0
    dialogitem EDITBOX_CLASSNAME, EDITBOX_NAME,\
      IDC_EDITBOX1, 30, 51, 54, 15, STYLE_EDITBOX,\
      WS_EX_CLIENTEDGE
    dialogitem EDITBOX_CLASSNAME, EDITBOX_NAME,\
      IDC_EDITBOX2, 108, 51, 54, 15, STYLE_EDITBOX,\
      WS_EX_CLIENTEDGE
    dialogitem EDITBOX_CLASSNAME, EDITBOX_NAME,\
      IDC_EDITBOX3, 183, 51, 54, 15, STYLE_EDITBOX,\
      WS_EX_CLIENTEDGE
    dialogitem STATIC_CLASSNAME, STATIC1_NAME, 0, 168, 54,\
      12, 9, STYLE_STATIC, 0
    dialogitem STATIC_CLASSNAME, STATIC2_NAME, 0, 93, 54,\
      12, 9, STYLE_STATIC, 0
  enddialog

section '.idata' import data readable writeable
  library\
    kernel, 'kernel32.dll',\
    user, 'user32.dll'

  import kernel,\
    GetModuleHandleA, 'GetModuleHandleA',\
    ExitProcess, 'ExitProcess',\
    lstrlenA, 'lstrlenA'

  import user,\
    DialogBoxParamA, 'DialogBoxParamA',\
    EndDialog, 'EndDialog',\
    SendDlgItemMessageA, 'SendDlgItemMessageA'
