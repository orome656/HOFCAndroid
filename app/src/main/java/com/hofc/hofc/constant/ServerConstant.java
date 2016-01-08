package com.hofc.hofc.constant;

import com.hofc.hofc.BuildConfig;

public class ServerConstant {
	public static String SERVER_URL_PREFIX = BuildConfig.SERVER_URL_PREFIX;
	public static String SERVER_URL = BuildConfig.SERVER_URL;
    public static int SERVER_PORT = BuildConfig.SERVER_PORT;
	public static final String[] CALENDRIER_CONTEXT = {"Calendrier/equipe1", "Calendrier/equipe2", "Calendrier/equipe3"};
	public static final String[] CLASSEMENT_CONTEXT = {"Classement/equipe1", "Classement/equipe2", "Classement/equipe3"};
	public static final String ACTUS_CONTEXT = "Actu";
    public static final String NOTIF_CONTEXT = "Notification";
    public static final String PARSE_PAGE_CONTEXT = "ParsePage";
    public static final String AGENDA_CONTEXT = "Agenda";
	public static final String MATCH_CONTEXT = "MatchInfos";
	public static final String JOURNEE_CONTEXT = "Journee";
	public static final int NOMBRE_JOUR_SYNCHRO = 7;
	public static final String PARAMS_CONTEXT = "Params";
}
