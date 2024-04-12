format pe gui
entry start

include 'win32w.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or BS_DEFPUSHBUTTON
CLASSNAME_BUTTON EQU 'BUTTON'

WINDOW_NAME EQU 'Windows App'
BUTTON1_NAME EQU 'Uruchom'
BUTTON2_NAME EQU 'Zakoñcz'

IDC_DIALOG EQU 100
IDC_BUTTON1 EQU 101
IDC_BUTTON2 EQU 102

section '.data' data readable writeable
  hThisInstance dd 0
  aplikacja du 'C:\Windows\system32\calc.exe', 0
  startUpInfo STARTUPINFO
  processInformation PROCESS_INFORMATION

section '.text' code readable executable
start:
  stdcall [GetModuleHandleW], 0
  mov [hThisInstance], eax
  stdcall [DialogBoxParamW], eax, IDC_DIALOG, 0,\
  WindowProcedure, 0
  stdcall [ExitProcess], 0

proc WindowProcedure stdcall uses ebx esi edi,\
  hWnd, Msg, wParam, lParam
  cmp dword [Msg], WM_COMMAND
  je WindowProcedure.wm_command
  cmp dword [Msg], WM_CLOSE
  je WindowProcedure.wm_close
  jmp WindowProcedure.return
.wm_command:
  cmp dword [wParam], IDC_BUTTON1
  je WindowProcedure.button1
  cmp dword [wParam], IDC_BUTTON2
  je WindowProcedure.button2
  jmp WindowProcedure.return
.button1:
  mov [startUpInfo.cb], 68
  mov [startUpInfo.lpReserved], 0
  mov [startUpInfo.lpDesktop], 0
  mov [startUpInfo.lpTitle], 0
  mov [startUpInfo.dwFlags], STARTF_USESHOWWINDOW
  mov [startUpInfo.cbReserved2], 0
  mov [startUpInfo.wShowWindow], SW_SHOWMINIMIZED
  stdcall [CreateProcessW], 0, aplikacja, 0, 0, 0, 0, 0, 0,\
    startUpInfo, processInformation
  jmp WindowProcedure.return
.button2:
  stdcall [TerminateProcess], [processInformation.hProcess],\
    0
  jmp WindowProcedure.return
.wm_close:
  stdcall [EndDialog], dword [hWnd], 0
.return:
  xor eax, eax
  ret
endp

section '.rsrc' data readable writeable resource
  directory RT_DIALOG, dialogs
  resource dialogs, IDC_DIALOG, LANG_ENGLISH +\
    SUBLANG_DEFAULT, szClassName
  dialog szClassName, WINDOW_NAME, 75, 87, 381, 210,\
    WS_OVERLAPPEDWINDOW, 0, 0, 'MS Sans Serif', 8
    dialogitem CLASSNAME_BUTTON, BUTTON1_NAME, IDC_BUTTON1,\
      51, 27, 102, 15, STYLE_BUTTON, 0
    dialogitem CLASSNAME_BUTTON, BUTTON2_NAME, IDC_BUTTON2,\
      210, 27, 107, 15, STYLE_BUTTON, 0
  enddialog

section '.idata' import data readable writeable
  library\
    kernel32, 'kernel32.dll',\
    user32, 'user32.dll'

  include 'api\kernel32.inc'
  include 'api\user32.inc'
