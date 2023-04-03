package com.intercorp.myapplication.fragment;

import static org.junit.Assert.*;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class RegisterFragmentTest {

    private RegisterFragment mRegister;

    @Before
    public void setUp() {
        mRegister = new RegisterFragment();
    }

    @Test
    public void registerNotNull() {
        assertNotNull(mRegister);
    }

    @Test
    public void setBodySearch() throws Exception{
        JsonObject body = new JsonObject();
        body.addProperty("nombre","Jesus");
        body.addProperty("apellido", "Iparraguirre");
        body.addProperty("edad","24");
        body.addProperty("nacimiento","120498");
        assertEquals(body,
                mRegister.setBodySearch("Jesus","Iparraguirre","24","120498"));
    }
}