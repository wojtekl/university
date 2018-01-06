format pe gui
entry start

include 'win32w.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or BS_DEFPUSHBUTTON

struct CRITICAL_SECTION
  dd ?
  dd ?
  dd ?
  dd ?
  dd ?
  dd ?
ends

macro DEC2RGB name, red, green, blue
{
  name EQU red or (green shl 8) or (blue shl 16)
}

DEC2RGB COLOR_TEXT1, 255, 0, 0
DEC2RGB COLOR_TEXT2, 0, 224, 0

section '.data' data readable writeable
  hThisInstance dd 0
  wincl WNDCLASS
  szClassName du 'WindowsApp', 0
  windowName du 'Windows App', 0
  hwnd dd 0
  messages MSG
  CLASSNAME_BUTTON du 'BUTTON', 0
  button1Name du 'jeden', 0
  button1Hwnd dd 0
  button2Name du 'dwa', 0
  button2Hwnd dd 0
  watek1Hwnd dd 0
  watek1Zakoncz db 0
  watek2Hwnd dd 0
  watek2Zakoncz db 0
  dC dd 0
  flaga db 0
  criticalSection CRITICAL_SECTION
  napisX dd 30
  napisY dd 30
  napisPusty du '', 0
  napisKoniec POINT
  napis du 'Kolorowy napis', 0
  napisAdres dd 0


section '.text' code readable writeable
start:
  stdcall [GetModuleHandleW], 0
  mov [hThisInstance], eax
  mov [wincl.hInstance], eax
  mov [wincl.lpszClassName], szClassName
  mov [wincl.lpfnWndProc], WindowProcedure
  mov [wincl.style], CS_DBLCLKS
  stdcall [LoadIconW], 0, IDI_APPLICATION
  mov [wincl.hIcon], eax
  stdcall [LoadCursorW], 0, IDC_CROSS
  mov [wincl.hCursor], eax
  mov [wincl.lpszMenuName], 0
  mov [wincl.cbClsExtra], 0
  mov [wincl.cbWndExtra], 0
  mov [wincl.hbrBackground], COLOR_BACKGROUND
  stdcall [RegisterClassW], wincl
  stdcall [CreateWindowExW], 0, szClassName, windowName,\
    WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT, 544,\
    375, HWND_DESKTOP, 0, [hThisInstance], 0
  cmp eax, 0
  jz koniec
  mov [hwnd], eax
  stdcall [ShowWindow], eax, SW_SHOWNORMAL
  stdcall [UpdateWindow], eax
while_messages:
  stdcall [GetMessageW], messages, 0, 0, 0
  cmp eax, 0
  jz koniec
  stdcall [TranslateMessage], messages
  stdcall [DispatchMessageW], messages
  jmp while_messages
koniec:
  stdcall [ExitProcess], [messages.wParam]

proc WindowProcedure stdcall uses ebx esi edi,\
  hWnd, Msg, wParam, lParam
  cmp dword [Msg], WM_CREATE
  je WindowProcedure.wm_create
  cmp dword [Msg], WM_COMMAND
  je WindowProcedure.wm_command
  cmp dword [Msg], WM_DESTROY
  je WindowProcedure.wm_destroy
  jmp WindowProcedure.default
.wm_create:
  stdcall [CreateWindowExW], 0, CLASSNAME_BUTTON,\
    button1Name, STYLE_BUTTON, 140, 100, 100, 40, [hWnd],\
    0, [hThisInstance], 0
  mov [button1Hwnd], eax
  stdcall [CreateWindowExW], 0, CLASSNAME_BUTTON,\
    button2Name, STYLE_BUTTON, 140, 160, 100, 40, [hWnd],\
    0, [hThisInstance], 0
  mov [button2Hwnd], eax
  jmp WindowProcedure.case_break
.wm_command:
  mov [watek1Zakoncz], 1
  mov [watek2Zakoncz], 1
  stdcall [TerminateThread], [watek1Hwnd], 0
  stdcall [TerminateThread], [watek2Hwnd], 0
  stdcall [GetDC], [hWnd]
  mov [dC], eax
  stdcall [SetBkColor], eax, COLOR_BACKGROUND
  stdcall [TextOutW], [dC], 130, napisY, napisPusty, 30
  stdcall [GetTextExtentPoint32W], [dC], napisPusty, 30,\
    napisKoniec
  cmp [flaga], 0
  jne WindowProcedure.dalej
  mov [flaga], 1
  stdcall [InitializeCriticalSection], criticalSection
  xor eax, eax
.dalej:
  mov eax, dword [lParam]
  cmp eax, [button1Hwnd]
  je WindowProcedure.button1
  cmp eax, [button2Hwnd]
  je WindowProcedure.button2
  jmp WindowProcedure.case_break
.button1:
  lea eax, [napis]
  mov [napisAdres], eax
  mov [napisX], 130
  stdcall [CreateThread], 0, 0, watek1, eax, 0, watek1Hwnd
  jmp WindowProcedure.case_break
.button2:
  lea eax, [napis]
  mov [napisAdres], eax
  mov [napisX], 130
  stdcall [CreateThread], 0, 0, watek1, eax, 0, watek1Hwnd
  stdcall [CreateThread], 0, 0, watek2, eax, 0, watek2Hwnd
  jmp WindowProcedure.case_break
.wm_destroy:
  stdcall [DeleteCriticalSection], criticalSection
  stdcall [PostQuitMessage], 0
  jmp WindowProcedure.case_break
.default:
  stdcall [DefWindowProcW], [hWnd], [Msg], [wParam],\
    [lParam]
  jmp WindowProcedure.return
.case_break:
  xor eax, eax
.return:
  ret
endp

proc watek1
  mov [watek1Zakoncz], 0
.pisz:
  mov ebx, [napisAdres]
  cmp byte [ebx], 0
  je watek1.koniec
  stdcall [EnterCriticalSection], criticalSection
  stdcall [GetDC], [hwnd]
  mov [dC], eax
  stdcall [SetBkColor], eax, COLOR_BACKGROUND
  stdcall [SetTextColor], [dC], COLOR_TEXT1
  stdcall [TextOutW], [dC], [napisX], [napisY],\
    [napisAdres], 1
  stdcall [GetTextExtentPoint32W], [dC], [napisAdres], 1,\
    napisKoniec
  mov eax, [napisKoniec.x]
  add [napisX], eax
  inc [napisAdres]
  inc [napisAdres]
  stdcall [DeleteDC], [dC]
  stdcall [LeaveCriticalSection], criticalSection
  stdcall [Sleep], 1000
  cmp [watek1Zakoncz], 1
  jne watek1.pisz
.koniec:
  ret
endp

proc watek2
  mov [watek2Zakoncz], 0
.pisz:
  mov ebx, [napisAdres]
  cmp byte [ebx], 0
  je watek2.koniec
  stdcall [EnterCriticalSection], criticalSection
  stdcall [GetDC], [hwnd]
  mov [dC], eax
  stdcall [SetBkColor], eax, COLOR_BACKGROUND
  stdcall [SetTextColor], [dC], COLOR_TEXT2
  stdcall [TextOutW], [dC], [napisX], [napisY],\
    [napisAdres], 1
  stdcall [GetTextExtentPoint32W], [dC], [napisAdres], 1,\
    napisKoniec
  mov eax, [napisKoniec.x]
  add [napisX], eax
  inc [napisAdres]
  inc [napisAdres]
  stdcall [DeleteDC], [dC]
  stdcall [LeaveCriticalSection], criticalSection
  stdcall [Sleep], 1000
  cmp [watek2Zakoncz], 1
  jne watek2.pisz
.koniec:
  ret
endp

section '.idata' import data readable writeable
  library\
    kernel32, 'kernel32.dll',\
    user32, 'user32.dll',\
    gdi32, 'gdi32.dll'

  include 'api\kernel32.inc'
  include 'api\user32.inc'
  include 'api\gdi32.inc'
