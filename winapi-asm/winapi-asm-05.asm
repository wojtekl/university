format pe gui
entry start

include 'win32a.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or BS_DEFPUSHBUTTON
STYLE_LISTBOX EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or WS_THICKFRAME or WS_BORDER or WS_VSCROLL
STYLE_EDITBOX EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or WS_BORDER

WINDOW_NAME EQU "Windows App"
BUTTON_NAME EQU "Przycisk"
BUTTON_CLASSNAME EQU "BUTTON"
LISTBOX_NAME EQU ""
LISTBOX_CLASSNAME EQU "LISTBOX"
EDITBOX_NAME EQU ""
EDITBOX_CLASSNAME EQU "EDIT"

IDC_BUTTON EQU 101
IDC_LISTBOX EQU 102
IDC_EDITBOX EQU 103
IDC_DIALOG EQU 104

section '.bss' data readable writeable
  hThisInstance dd 0
  bufor: times 100 db 0

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
  cmp dword [ebp + 0ch], WM_INITDIALOG
  je wm_initdialog
  cmp dword [ebp + 0ch], WM_COMMAND
  je wm_command
  cmp dword [ebp + 0ch], WM_CLOSE
  je wm_close
  jmp return
wm_initdialog:
  push 0
  push [hThisInstance]
  call [LoadIconA]
  push eax
  push 0
  push WM_SETICON
  push dword [ebp + 8h]
  call [SendMessageA]
  jmp return
wm_command:
  cmp dword [ebp + 10h], IDC_BUTTON
  jne return
  push bufor
  push 100
  push WM_GETTEXT
  push IDC_EDITBOX
  push dword [ebp + 8h]
  call [SendDlgItemMessageA]
  push bufor
  push 0
  push LB_ADDSTRING
  push IDC_LISTBOX
  push dword [ebp + 8h]
  call [SendDlgItemMessageA]
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

section '.rsrc' data readable resource
  directory RT_DIALOG, dialogs
  resource dialogs, IDC_DIALOG,\
    LANG_ENGLISH + SUBLANG_DEFAULT, szClassName
  dialog szClassName, WINDOW_NAME, 10, 10, 313, 223,\
    WS_OVERLAPPEDWINDOW, 0, 0, "MS Sans Serif", 8
    dialogitem BUTTON_CLASSNAME, BUTTON_NAME, IDC_BUTTON,\
      120, 90, 42, 15, STYLE_BUTTON, 0
    dialogitem LISTBOX_CLASSNAME, LISTBOX_NAME,\
      IDC_LISTBOX, 213, 90, 94, 57, STYLE_LISTBOX, 0
    dialogitem EDITBOX_CLASSNAME, EDITBOX_NAME,\
      IDC_EDITBOX, 6, 90, 57, 12, STYLE_EDITBOX, 0
  enddialog


section '.idata' import data readable writeable
  library\
    kernel, 'kernel32.dll',\
    user, 'user32.dll'

  import kernel,\
    GetModuleHandleA, 'GetModuleHandleA',\
    ExitProcess, 'ExitProcess'

  import user,\
    LoadIconA, 'LoadIconA',\
    SendMessageA, 'SendMessageA',\
    DialogBoxParamA, 'DialogBoxParamA',\
    EndDialog, 'EndDialog',\
    SendDlgItemMessageA, 'SendDlgItemMessageA'
