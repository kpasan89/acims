/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amis.controller;

/**
 *
 * @author Madhuka
 */
import com.amis.controller.util.JsfUtil;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.print.DocFlavor;

@ManagedBean
@SessionScoped
public class LdapEntryController {

    private DirContext dirContext = null;

    public LdapEntryController() {
        System.out.println("constructer called");
        try {
            String url = "ldap://172.16.60.33:389";
            String conntype = "simple";
            String AdminDn = "cn=admin,dc=test,dc=com";
            String password = "abc@123";

            Hashtable<String, String> environment = new Hashtable<String, String>();

            environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            environment.put(Context.PROVIDER_URL, url);
            environment.put(Context.SECURITY_AUTHENTICATION, conntype);
            environment.put(Context.SECURITY_PRINCIPAL, AdminDn);
            environment.put(Context.SECURITY_CREDENTIALS, password);

            dirContext = new InitialDirContext(environment);
            System.out.println("context created");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    Attribute attribute;

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    Attribute aid;
    Attribute acn;
    Attribute asn;
    Attribute amail;

    String aidString;
    String acnString;
    String asnString;
    String amailString;

    String userId;
    String username;
    boolean logged;

    public String getUsername() {
        if (username == null) {
            username = new String();
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getUserId() {
        if (userId == null) {
            userId = new String();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAidString() {
        if (aidString == null) {
            aidString = new String();
        }
        return aidString;
    }

    public void setAidString(String aidString) {
        this.aidString = aidString;
    }

    public String getAcnString() {
        if (acnString == null) {
            acnString = new String();
        }
        return acnString;
    }

    public void setAcnString(String acnString) {
        this.acnString = acnString;
    }

    public String getAsnString() {
        if (asnString == null) {
            asnString = new String();
        }
        return asnString;
    }

    public void setAsnString(String asnString) {
        this.asnString = asnString;
    }

    public String getAmailString() {
        if (amailString == null) {
            amailString = new String();
        }
        return amailString;
    }

    public void setAmailString(String amailString) {
        this.amailString = amailString;
    }

    public Attribute getAid() {
        return aid;
    }

    public void setAid(Attribute aid) {
        this.aid = aid;
    }

    public Attribute getAcn() {
        return acn;
    }

    public void setAcn(Attribute acn) {
        this.acn = acn;
    }

    public Attribute getAsn() {
        return asn;
    }

    public void setAsn(Attribute asn) {
        this.asn = asn;
    }

    public Attribute getAmail() {
        return amail;
    }

    public void setAmail(Attribute amail) {
        this.amail = amail;
    }

    public String login() {
        if (username.isEmpty() || userId.isEmpty()) {
            JsfUtil.addErrorMessage("Empty Fields");
        } else {
            getData(userId);
            if (getAidString() == null || getAcnString() == null) {
                JsfUtil.addErrorMessage("No Internet connection");
            }
            if (getAidString().equals(getUserId()) && getAcnString().equals(getUsername())) {
                logged = true;
                JsfUtil.addSuccessMessage("Logged Successfully");
            } else {
                logged = false;
                JsfUtil.addErrorMessage("Wrong Details");
            }
        }
        return "index";
    }

    public String logout() {
        logged = false;
        userId = null;
        username = null;
        return "index";
    }

    public void getData(String userId) {
        System.out.println("userId in getData Method = " + userId);
        String[] data = new String[4];
        try {
            String searchDn = "uid=" + userId + "," + "ou=onservice,ou=person,dc=test,dc=com";
            // uid=142,ou=alzebra,dc=mathsdep,dc=college,dc=org,dc=in
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration results2 = dirContext.search(searchDn, "(uid=" + userId + ")", searchControls);

            while (results2.hasMore()) {
                SearchResult sr2 = (SearchResult) results2.next();
                Attributes attrs = sr2.getAttributes();

                aid = attrs.get("uid");
                System.out.println("attrId = " + aid);

                acn = attrs.get("cn");
                System.out.println("attrCn = " + acn);

                asn = attrs.get("sn");
                System.out.println("attrSn = " + asn);

                amail = attrs.get("mail");
                System.out.println("attrMail = " + amail);

//                Attribute attrMobile = attrs.get("Mobile");
//                System.out.println(attrMobile);
//                Attribute attrName = attrs.get("givenname");
//                System.out.println(attrName);
//                Attribute attrPrslTitle = attrs.get("PersonalTitle");
//                System.out.println(attrPrslTitle);
//                Attribute attrPassword = attrs.get("userPassword");
//                System.out.println(attrPassword);
//                Attribute attrStatus = attrs.get("userstatus");
//                System.out.println(attrStatus);
                data[0] = (String) aid.get();
                setAidString(data[0]);
                data[1] = (String) acn.get();
                setAcnString(data[1]);
                data[2] = (String) asn.get();
                setAsnString(data[2]);
                data[3] = (String) amail.get();
                setAmailString(data[3]);

//                data[4] = (String) attrMobile.get();
//                data[5] = (String) attrName.get();
//                data[6] = (String) attrPrslTitle.get();
//                data[7] = (String) attrPassword.get();
//                data[8] = (String) attrStatus.get();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        String entryFromLDAP[] = new LdapEntryController().getData("145");
//        for (String data : entrtring entryFromLDAP[] = new LdapEntryController().getData("145");yFromLDAP) {
//            System.out.println(data);
//        }
//
//    }
}
