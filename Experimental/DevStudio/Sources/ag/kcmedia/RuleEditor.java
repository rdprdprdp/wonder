//
// Sources/ag/kcmedia/RuleEditor.java: Class file for WO Component 'RuleEditor'
// Project DevStudio
//
// Created by ak on Thu Jul 25 2002
//
package ag.kcmedia;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.ERD2WRuleEditorModel;
import com.webobjects.directtoweb.*;
import java.io.File;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.logging.ERXLogger;
import java.util.*;

public class RuleEditor extends WOComponent {
    static final ERXLogger log = ERXLogger.getERXLogger(RuleEditor.class,"components");

    public WODisplayGroup group;
    public String qualifierString;
    public String error;
    public EOQualifier qualifier;
    public ERD2WExtendedRule currentRule;
    public ERD2WRuleEditorModel model;
    public String fileName;
    
    public RuleEditor(WOContext context) {
        super(context);
    }

    public void setRuleFileName(String value) {
        fileName = value;
        model = new ERD2WRuleEditorModel(new File(fileName));
        group = new WODisplayGroup();
        group.setObjectArray(model.publicRules());
        group.setDefaultStringMatchFormat("*%@*");
    }
    
    public boolean isStateless() { return false;}
    public boolean synchronizesVariablesWithBindings() { return false;}
    
    public EOQualifier qualifier() {
        if(qualifier == null) {
            try {
                qualifier = EOQualifier.qualifierWithQualifierFormat(qualifierString, null);
            } catch (Throwable ex) {
                error = ex.getMessage();
                System.err.println(ex);
            }
        }
        return qualifier;
    }
    public String qualifierString() {
        if(qualifierString == null && qualifier() != null) {
            String decimalString = "(java.math.BigDecimal)'";
            qualifierString = qualifier().toString();
            int offset;
            while((offset = qualifierString.indexOf(decimalString)) >= 0) {
                qualifierString = qualifierString.substring(0,offset) + qualifierString.substring(offset+decimalString.length());
                offset = qualifierString.indexOf("'",offset);
                qualifierString = qualifierString.substring(0,offset) + qualifierString.substring(offset+1);
            }
        }
        return qualifierString;
    }
    public Rule selectedRule() {
        return (Rule)group.selectedObject();
    }
    public void setSelectedRule(Rule rule) {
        group.setSelectedObject(rule);
        qualifier = rule.lhs();
        qualifierString = null;
    }
    public String selectedRuleAsString() {
        if(selectedRule() != null)
            return objectAsString(selectedRule());
        return null;
    }
    public NSArray modelKeys() {
        Class c = ERXArrayUtilities.class;
        NSArray keys = (NSArray)model.publicRules().valueForKey("rhsKeyPath");
        keys = (NSArray)(new NSSet(keys).allObjects().valueForKey("@sort.toString"));
        return keys;
    }
    public String fixString(Object o) {
        String fix = "'" + o + "'";
        return ERXStringUtilities.replaceStringByStringInString("*", "[*]", fix);
    }
    public NSArray modelPages() {
        Enumeration e = model.publicDynamicPages().elements();
        NSMutableArray arr = new NSMutableArray();
        while(e.hasMoreElements()) {
            arr.addObject(fixString(e.nextElement()));
        }
        return arr;
    }
    public NSArray modelTasks() {
        Enumeration e = model.publicTasks();
        NSMutableArray arr = new NSMutableArray();
        while(e.hasMoreElements())
            arr.addObject(fixString(e.nextElement()));
        return arr;
    }
    public WOComponent pushQualifier() {
        qualifier = null;
        error = null;
        selectedRule().setLhs(qualifier());
        return null;
    }
    public WOComponent selectRule() {
        setSelectedRule(currentRule);
        return null;
    }
    public WOComponent cloneRule() {
        currentRule = currentRule.cloneRule();
        group.insertObjectAtIndex(currentRule, 0);
        setSelectedRule(currentRule);
        return null;
    }
    public WOComponent addRule() {
        ERD2WExtendedRule rule = new ERD2WExtendedRule();
        rule.setAuthor(50);
        rule.setRhs(new Assignment("test", null));
        group.insertObjectAtIndex(rule, 0);
        setSelectedRule(rule);
        return null;
    }
    public WOComponent deleteRule() {
        Rule oldRule = selectedRule();
        setSelectedRule(currentRule);
        group.delete();
        setSelectedRule(oldRule);
        return null;
    }
    public String objectAsString(Rule o) {
        EOKeyValueArchiver eokeyvaluearchiver = new EOKeyValueArchiver();
        o.encodeWithKeyValueArchiver(eokeyvaluearchiver);
        return NSPropertyListSerialization._Utilities
            .stringFromPropertyList(eokeyvaluearchiver.dictionary());
    }
    public WOComponent eval() {
        qualifier = null;
        error = null;
        try {
            qualifier = EOQualifier.qualifierWithQualifierFormat(qualifierString, null);
        } catch (Throwable ex) {
            error = ex.getMessage();
        }
        return null;
    }
    public WOActionResults editor()
    {
        WOComponent editor = pageWithName("ERXQualifierEditor");
        if(selectedRule() != null)
            editor.takeValueForKey( selectedRule().lhs(),"qualifier");
        return editor;
    }
}