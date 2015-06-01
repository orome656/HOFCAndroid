package com.hofc.hofc.constant;

import com.hofc.hofc.BuildConfig;

public class ServerConstant {
	public static String SERVER_URL_PREFIX = BuildConfig.SERVER_URL_PREFIX;
	public static String SERVER_URL = BuildConfig.SERVER_URL;
    public static int SERVER_PORT = BuildConfig.SERVER_PORT;
	public static final String CALENDRIER_CONTEXT = "calendrier";
	public static final String CLASSEMENT_CONTEXT = "classement";
	public static final String ACTUS_CONTEXT = "actus";
    public static final String NOTIF_CONTEXT = "registerPush";
    public static final String PARSE_PAGE_CONTEXT = "parsePage";
    public static final String AGENDA_CONTEXT = "agendadistrict";
	public static final String MATCH_CONTEXT = "matchinfosdistrict";
	public static final String JOURNEE_CONTEXT = "journee";
	public static final int NOMBRE_JOUR_SYNCHRO = 7;
	public static String PARAMS_CONTEXT = "params";
}
