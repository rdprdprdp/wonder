package er.jquerymobile.components;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXStatelessComponent;

/**
 * data-role  header これを設定することでヘッダーになります。
 */
@SuppressWarnings("serial")
public class ERQMHeaderBar extends ERXStatelessComponent {

  protected static final Logger log = Logger.getLogger(ERQMHeaderBar.class);

  //********************************************************************
  //  Constructor
  //********************************************************************

  public ERQMHeaderBar(WOContext aContext) {
    super(aContext);
  }

  //********************************************************************
  //  Methods
  //********************************************************************

  /**
   * data-position
   * 
   * @return "fixed" 又は "inline"
   */
  public String dataPosition() {
    if(hasBinding("isFixed")) {
      return (booleanValueForBinding("isFixed", false) ? "fixed" : "inline");
    }
    return "inline";
  }

  public boolean hasAction() {
    return hasBinding("rightButton-action");
  }

  public boolean hasLinkResource() {
    return hasBinding("rightButton-linkResource");
  }

}
