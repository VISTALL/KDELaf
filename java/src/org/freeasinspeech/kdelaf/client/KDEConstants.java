package org.freeasinspeech.kdelaf.client;

/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KDEConstants
{
	public static final int MAX_PACKET_SIZE = 20000;
		
	public static final int OP_KILL										= 1000;
	public static final int OP_GET_BASE_DEFAULTS						= 1001;
	public static final int OP_GET_COLOR_BY_KEY						= 1002;
	public static final int OP_GET_BUTTON_IMAGE						= 1003;
	public static final int OP_GET_PIXEL_METRIC						= 1004;
	public static final int OP_GET_KPIXEL_METRIC						= 1005;
	public static final int OP_GET_TAB_IMAGE							= 1006;
	public static final int OP_GET_MENUITEM_IMAGE					= 1007;
	public static final int OP_GET_CHECKBOXMENUITEM_IMAGE			= 1008;
	public static final int OP_GET_RADIOBUTTONMENUITEM_IMAGE		= 1009;
	public static final int OP_GET_MENU_IMAGE							= 1010;
	public static final int OP_GET_MENUBAR_IMAGE						= 1011;
	public static final int OP_GET_SIMPLEPREFERREDSIZE				= 1012;
	public static final int OP_GET_TOOLTIPDEFAULTS					= 1013;
	public static final int OP_GET_TOGGLEBUTTON_IMAGE				= 1014;
	public static final int OP_GET_TOOLBARSEPARATOR_IMAGE			= 1015;
	public static final int OP_GET_TOOLBARBACKGROUND_IMAGE		= 1016;
	public static final int OP_GET_TOOLBARHANDLE_IMAGE				= 1017;
	public static final int OP_GET_TREEDEFAULTS						= 1018;
	public static final int OP_GET_ICONPATHS							= 1019;
	public static final int OP_GET_COMBOBOX_DISPLAY_SIZE			= 1020;
	public static final int OP_GET_COMBOBOX_VALUE_RECT				= 1021;
	public static final int OP_GET_COMBOBOX_IMAGE					= 1022;
	public static final int OP_GET_TEXTFIELDDEFAULTS				= 1023;
	public static final int OP_GET_EXCLUSIVE_INDICATOR_DIM		= 1024;
	public static final int OP_GET_INDICATOR_DIM						= 1025;
	public static final int OP_GET_RADIOBUTTON_IMAGE				= 1026;
	public static final int OP_GET_CHECKBOX_IMAGE					= 1027;
	public static final int OP_GET_LABELDEFAULTS						= 1028;
	public static final int OP_GET_COMBOBOXDEFAULTS					= 1029;
	public static final int OP_GET_LOCALEPATHS						= 1030;
	public static final int OP_GET_MIMEPATHS							= 1031;
	public static final int OP_GET_MENUBUTTONINDICATOR_IMAGE		= 1032;
	public static final int OP_GET_CONFIGPATHS						= 1033;
	public static final int OP_GET_SCROLLBARTHUMB_IMAGE			= 1034;
	public static final int OP_GET_SCROLLBAR_METRIS					= 1035;
	public static final int OP_GET_SCROLLBARNEXT_IMAGE				= 1036;
	public static final int OP_GET_SCROLLBARPREV_IMAGE				= 1037;
	public static final int OP_GET_SCROLLBARTRACK_IMAGE			= 1038;
	public static final int OP_GET_SPINNER_IMAGE						= 1039;
	public static final int OP_GET_COMMON_IMAGE						= 1040;
	public static final int OP_GET_TITLE_BUTTON						= 1041;
	public static final int OP_GET_TABLEHEADER_IMAGE				= 1042;
	public static final int OP_GET_PROGRESSBAR_IMAGE				= 1043;
	public static final int OP_GET_SLIDERHANDLE_IMAGE				= 1044;
	public static final int OP_GET_SLIDERGROOVE_IMAGE				= 1045;
	public static final int OP_GET_THUMBTRACKOFFSET				= 1046;
	public static final int OP_GET_STYLE_NAME				= 1047;
	public static final int OP_GET_KDE_VERSION				= 1048;
	public static final int OP_GET_SCROLLBAR_IMAGE				= 1049;
	public static final int OP_GET_BUTTON_PREF_SZ				= 1050;
	public static final int OP_GET_TOOLBUTTON_PREF_SZ			= 1051;
		
		
	
	public static final int WT_BUTTON					= 0;
	public static final int WT_CHECKBOXMENUITEM		= 1;
	public static final int WT_CHECKBOX					= 2;
	public static final int WT_MENUBAR					= 3;
	public static final int WT_MENUITEM					= 4;
	public static final int WT_MENU						= 5;
	public static final int WT_POPUPMENU				= 6;
	public static final int WT_PROGRESSBAR				= 7;
	public static final int WT_RADIOBUTTONMENUITEM	= 8;
	public static final int WT_RADIOBUTTON				= 9;
	public static final int WT_TEXTFIELD				= 10;
	public static final int WT_TOGGLEBUTTON			= 11;
	public static final int WT_TABBEDPANE				= 12;
	public static final int WT_TOOLBAR					= 13;
	public static final int WT_COMBOBOX					= 14;
	public static final int WT_SPINNER					= 15;
		
		

	public static final int CR_DESKTOP						= 500;
	public static final int CR_ACTIVECAPTION				= 501;
	public static final int CR_ACTIVECAPTIONTEXT			= 502;
	public static final int CR_ACTIVECAPTIONBORDER		= 503;
	public static final int CR_INACTIVECAPTION			= 504;
	public static final int CR_INACTIVECAPTIONTEXT		= 505;
	public static final int CR_INACTIVECAPTIONBORDER	= 506;
	public static final int CR_WINDOW						= 507;
	public static final int CR_WINDOWBORDER				= 508;
	public static final int CR_WINDOWTEXT					= 509;
	public static final int CR_MENU							= 510;
	public static final int CR_MENUTEXT						= 511;
	public static final int CR_TEXT							= 512;
	public static final int CR_TEXTTEXT						= 513;
	public static final int CR_TEXTHIGHLIGHT				= 514;
	public static final int CR_TEXTHIGHLIGHTTEXT			= 515;
	public static final int CR_TEXTINACTIVETEXT			= 516;
	public static final int CR_CONTROL						= 517;
	public static final int CR_CONTROLTEXT					= 518;
	public static final int CR_CONTROLHIGHLIGHT			= 519;
	public static final int CR_CONTROLLTHIGHLIGHT		= 520;
	public static final int CR_CONTROLSHADOW				= 521;
	public static final int CR_CONTROLDKSHADOW			= 522;
	public static final int CR_SCROLLBAR					= 523;
	public static final int CR_INFO							= 524;
	public static final int CR_INFOTEXT						= 525;
	
	
	
	public static final int JPM_ButtonMargin		= 2000;
	public static final int JPM_TabBarTabVSpace		= 2001;
	public static final int JPM_TabBarTabHSpace		= 2002;
	public static final int JPM_MenuButtonIndicator		= 2003;
	public static final int JPM_DockWindowSeparatorExtent	= 2004;
	public static final int JPM_DockWindowHandleExtent	= 2005;
	public static final int JPM_TableHeaderH		= 2006;
	public static final int JPM_ButtonH			= 2007;
	public static final int JPM_ButtonShiftHorizontal	= 2008;
	public static final int JPM_ButtonShiftVertical		= 2009;
		
	
	public static final int JSC_TitleBarSysMenu			= 5000;
	public static final int JSC_TitleBarMinButton		= 5001;
	public static final int JSC_TitleBarMaxButton		= 5002;
	public static final int JSC_TitleBarCloseButton		= 5003;
	public static final int JSC_TitleBarLabel				= 5004;
	public static final int JSC_TitleBarNormalButton	= 5005;
	public static final int JSC_TitleBarShadeButton		= 5006;
	public static final int JSC_TitleBarUnshadeButton	= 5007;
	public static final int JSC_MIN = 5000;
	public static final int JSC_LEN = 8;
		
		
	public static final int SZ_MENUBAR		= 3000;
	public static final int SZ_HPROGRESSBAR		= 3001;
	public static final int SZ_VPROGRESSBAR		= 3002;
	public static final int SZ_HSCROLLBAR		= 3003;
	public static final int SZ_HSCROLLBARMIN	= 3004;
	public static final int SZ_VSCROLLBAR		= 3005;
	public static final int SZ_VSCROLLBARMIN	= 3006;
		
	
	public static final int CI_ERROR_ICON		= 4000;
	public static final int CI_INFO_ICON		= 4001;
	public static final int CI_WARNING_ICON		= 4002;
	public static final int CI_QUESTION_ICON	= 4003;
	public static final int CI_MAXIMIZE_ICON	= 4004;
	public static final int CI_MINIMIZE_ICON	= 4005;
	public static final int CI_CLOSE_ICON		= 4006;
	public static final int CI_ICONIFY_ICON		= 4007;
	public static final int CI_TREEEXPANDER_ON	= 4008;
	public static final int CI_TREEEXPANDER_OFF	= 4009;
	public static final int CI_TREEBRANCH_H		= 4010;
	public static final int CI_TREEBRANCH_V		= 4011;
		
}
