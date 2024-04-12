format pe gui
entry start

include 'win32w.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or BS_DEFPUSHBUTTON
STYLE_EDITBOX EQU WS_CHILD or WS_VISIBLE or WS_BORDER or\
  WS_TABSTOP

section '.data' data readable writeable
  hThisInstance dd 0
  wincl WNDCLASS
  szClassName du 'WindowsApp', 0
  windowName du 'Windows App', 0
  hwnd dd 0
  messages MSG
  CLASSNAME_BUTTON du 'BUTTON', 0
  CLASSNAME_EDITBOX du 'EDIT', 0
  buttonName du 'Uruchom', 0
  buttonHwnd dd 0
  editBoxName du '', 0
  editBoxHwnd dd 0
  bibliotekaNazwa du 'winapi-asm-11-dll.dll', 0
  bibliotekaUchwyt dd 0
  funkcjaNazwa db 'funkcja', 0
  funkcjaUchwyt dd 0
  bladNazwa du 'B³¹d!', 0
  bladKomunikat du 'B³¹d biblioteki dll.', 0

section '.text' code readable executable
start:
  stdcall [LoadLibraryW], bibliotekaNazwa
  cmp eax, 0
  je blad
  mov [bibliotekaUchwyt], eax
  stdcall [GetProcAddress], [bibliotekaUchwyt], funkcjaNazwa
  cmp eax, 0
  je blad
  mov [funkcjaUchwyt], eax
  stdcall [GetModuleHandleW], 0
  mov [hThisInstance], eax
  mov [wincl.hInstance], eax
  mov [wincl.lpszClassName], szClassName
  mov [wincl.lpfnWndProc], WindowProcedure
  mov [wincl.style], CS_DBLCLKS
  stdcall [LoadIconW], 0, IDI_APPLICATION
  mov [wincl.hIcon], eax
  stdcall [LoadCursorW], 0, IDC_ARROW
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
  stdcall [FreeLibrary], [bibliotekaUchwyt]
  stdcall [ExitProcess], [messages.wParam]
blad:
  stdcall [MessageBoxW], 0, bladKomunikat, bladNazwa, 0
  jmp koniec

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
    buttonName, STYLE_BUTTON, 150, 150, 200, 20, [hWnd],\
    0, [hThisInstance], 0
  mov [buttonHwnd], eax
  stdcall [CreateWindowExW], 0, CLASSNAME_EDITBOX,\
    editBoxName, STYLE_EDITBOX, 150, 120, 200, 20, [hWnd],\
    0, [hThisInstance], 0
  mov [editBoxHwnd], eax
  jmp WindowProcedure.case_break
.wm_command:
  mov eax, [lParam]
  cmp eax, [buttonHwnd]
  je WindowProcedure.button
  jmp WindowProcedure.case_break
.button:
  stdcall [funkcjaUchwyt], [editBoxHwnd], 0
  jmp WindowProcedure.case_break
.wm_destroy:
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

section '.idata' data readable writeable import
library\
  kernel32, 'kernel32.dll',\
  user32, 'user32.dll'

include 'api\kernel32.inc'
include 'api\user32.inc'
