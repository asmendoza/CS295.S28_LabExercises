package edu.admu.cs295s28.LabActivity;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends AppCompatActivity {
    @ViewById(R.id.lblOutput)
    TextView lblOutput;

    @Extra("userName")
    String userName;

    @Extra("name")
    String name;

    @AfterViews
    public void init() {
        lblOutput.setText("Welcome " + name);
    }
}
