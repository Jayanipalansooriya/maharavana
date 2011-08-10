package com.example;

import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.text.XPageCursor;
import com.sun.star.text.XSentenceCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.text.XWordCursor;


public final class MahaRavana extends WeakBase
   implements com.sun.star.lang.XInitialization,
              com.sun.star.frame.XDispatch,
              com.sun.star.lang.XServiceInfo,
              com.sun.star.frame.XDispatchProvider
{
    private final XComponentContext m_xContext;
    private com.sun.star.frame.XFrame m_xFrame;
    private static final String m_implementationName = MahaRavana.class.getName();
    private static final String[] m_serviceNames = {
        "com.sun.star.frame.ProtocolHandler" };

     private Manager manager;

    public MahaRavana( XComponentContext context )
    {
        m_xContext = context;

        manager= new Manager();

        //new Messenger("");
        //checked>>> runs on startup and on select to go for a text document
    };

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(MahaRavana.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XInitialization:
    public void initialize( Object[] object )
        throws com.sun.star.uno.Exception
    {
        //new Messenger("");
        //checked>>> runs on startup and on select to go for a text document
        if ( object.length > 0 )
        {
            m_xFrame = (com.sun.star.frame.XFrame)UnoRuntime.queryInterface(
                com.sun.star.frame.XFrame.class, object[0]);
        }
    }

    // com.sun.star.frame.XDispatch:
     public void dispatch( com.sun.star.util.URL aURL,
                           com.sun.star.beans.PropertyValue[] aArguments )
    {
         //new Messenger(aArguments.toString());

         if ( aURL.Protocol.compareTo("com.example.maharavana:") == 0 )
        {
            if ( aURL.Path.compareTo("command") == 0 )
            {
                // add your own code here
                setTextDocument("testing");
                return;
            }
        }
    }

    public void addStatusListener( com.sun.star.frame.XStatusListener xControl,
                                    com.sun.star.util.URL aURL )
    {
        // add your own code here
    }

    public void removeStatusListener( com.sun.star.frame.XStatusListener xControl,
                                       com.sun.star.util.URL aURL )
    {
        // add your own code here
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch queryDispatch( com.sun.star.util.URL aURL,
                                                       String sTargetFrameName,
                                                       int iSearchFlags )
    {
        if ( aURL.Protocol.compareTo("com.example.maharavana:") == 0 )
        {
            if ( aURL.Path.compareTo("command") == 0 )
                return this;
        }
        return null;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch[] queryDispatches(
         com.sun.star.frame.DispatchDescriptor[] seqDescriptors )
    {
        int nCount = seqDescriptors.length;
        com.sun.star.frame.XDispatch[] seqDispatcher =
            new com.sun.star.frame.XDispatch[seqDescriptors.length];

        for( int i=0; i < nCount; ++i )
        {
            seqDispatcher[i] = queryDispatch(seqDescriptors[i].FeatureURL,
                                             seqDescriptors[i].FrameName,
                                             seqDescriptors[i].SearchFlags );
        }
        return seqDispatcher;
    }


      public void setTextDocument(String text) {
         XMultiComponentFactory xmcf = m_xContext.getServiceManager();
        Object desktop;


        try {
            desktop = xmcf.createInstanceWithContext("com.sun.star.frame.Desktop", m_xContext);
            XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, desktop);


            // retrieve the current component and access the controller
            XComponent xCurrentComponent = xDesktop.getCurrentComponent();


            // get the XModel interface from the component
            XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class, xCurrentComponent);


            // the model knows its controller
            XController xController = xModel.getCurrentController();


            // the controller gives us the TextViewCursor
            // query the viewcursor supplier interface
            XTextViewCursorSupplier xViewCursorSupplier =
                    (XTextViewCursorSupplier) UnoRuntime.queryInterface(
                    XTextViewCursorSupplier.class, xController);


            // get the cursor
            XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();


            // query its XPropertySet interface, we want to set character and paragraph properties
            XPropertySet xCursorPropertySet = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xViewCursor);


            // set the appropriate properties for character and paragraph style
            xCursorPropertySet.setPropertyValue("CharStyleName", "Quotation");
            xCursorPropertySet.setPropertyValue("ParaStyleName", "Quotations");


            // print the current page number – we need the XPageCursor interface for this
            XPageCursor xPageCursor = (XPageCursor) UnoRuntime.queryInterface(
                    XPageCursor.class, xViewCursor);




            // the model cursor is much more powerful, so
            // we create a model cursor at the current view cursor position with the following steps:
            // we get the Text service from the TextViewCursor, the cursor is an XTextRange and has
            // therefore a method getText()
            XText xDocumentText = xViewCursor.getText();


            // the text creates a model cursor from the viewcursor
            XTextCursor xModelCursor = xDocumentText.createTextCursorByRange(xViewCursor.getStart());

/////////////////////////////////////////////////////////////////////////////////////////////////////////
            
//            XSentenceCursor xSentenceCurser = (XSentenceCursor) UnoRuntime.queryInterface(
//                    XSentenceCursor.class, xModelCursor/*this is instant of XTextCursor*/);

            //xSentenceCurser.gotoStartOfSentence(true);
//            xSentenceCurser.gotoStart(true);

//            xSentenceCurser.gotoPreviousSentence(true);
            

            //xSentenceCurser.gotoStartOfSentence(true);
             // xSentenceCurser.setString("<strt>");
            //xSentenceCurser.gotoEndOfSentence(false);    //possition it in the first sentence

//           boolean hasNextSentence = true;
//           String textSentence = "";

//           while(hasNextSentence) {
           
//                xSentenceCurser.gotoEndOfSentence(true);
//                textSentence += xSentenceCurser.getString() + " #\n ";
                
//                hasNextSentence = xSentenceCurser.gotoNextSentence(false);
                //xSentenceCurser.setString("<end>");
//            }

           //xSentenceCurser.gotoEnd(true);
           //textSentence += xSentenceCurser.getString() + " | \n";

//            new Messenger(textSentence);

            //xSentenceCurser.setString("");

            /*
            boolean hasNextSentence = true; // get ready for the loop

            while(hasNextSentence) {
                xSentenceCurser.gotoEndOfSentence(true);
                String textSentence = xSentenceCurser.getString();

                new Messenger(textSentence);

                hasNextSentence = xSentenceCurser.gotoNextSentence(false);
            }
            */
            //System.out.println(lastWord);
/////////////////////////////////////////////////////////////////////////////////////////////////////////


            XWordCursor xWordCurser = (XWordCursor) UnoRuntime.queryInterface(
                    XWordCursor.class, xModelCursor);


            xWordCurser.gotoPreviousWord(true);


            String fullString = xWordCurser.getText().getString();
            //System.out.println(lastWord);

            /*
            Tag_Display tagList =  new Tag_Display();
            tagList.setVisible(true);

            tagList.tag_and_write(fullString);
            */

//grammar check
          manager.proceess_string(fullString);


            //String str= xWordCurser.getText().toString();


            //xWordCurser.setString(text);
            


        } catch (Exception e) {

            //System.out.println("word not found");

        }



}

}
