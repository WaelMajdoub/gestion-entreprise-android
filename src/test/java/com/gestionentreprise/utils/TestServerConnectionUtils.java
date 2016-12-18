package com.gestionentreprise.utils;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

public class TestServerConnectionUtils {
    private static final String URL_BASE = "http://192.168.133.113/gestion-entreprise/app.php/";
    private static final String COMPTABILITE_SUFFIXE = "comptabilite/mobile/";
    private static final String PARAMETRAGE_SUFFIXE = "parametrage/mobile/";

    @Test
    public void checkServerUrl() {
	Properties properties = new Properties();
	properties.put("server.url", URL_BASE);
	ServerConnectionUtils.setProperties(properties);

	assertEquals("Comptabilite Url does not match",
	             URL_BASE + COMPTABILITE_SUFFIXE,
	             ServerConnectionUtils.getComptabiliteServerUrl());
	assertEquals("Comptabilite Url does not match",
	             URL_BASE + PARAMETRAGE_SUFFIXE,
	             ServerConnectionUtils.getParametrageServerUrl());
    }
}
