/*
 * PainterUtil.java
 *
 * Created on July 20, 2006, 1:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.editors;

//import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.Statement;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.apache.batik.ext.awt.LinearGradientPaint;
import org.apache.batik.ext.awt.RadialGradientPaint;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.editors.PainterPropertyEditor.*;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class PainterUtil {
    
    /** Creates a new instance of PainterUtil */
    public PainterUtil() {
    }
    
    public static Painter loadPainter(File file) throws FileNotFoundException, MalformedURLException {
        //Reader in = new FileReader(file);
        return loadPainter(file, file.toURL());
    }
    
    public static void main(String[] args) throws Exception {
        MattePainter mp = new MattePainter(Color.RED);
        savePainterToFile(mp, new File("test.xml"));
    }
    /*
    public static Painter loadPainter(URL uRL) throws IOException {
        return loadPainter(new InputStreamReader(uRL.openStream()),uRL);
    }
     */
    
    private static Painter loadPainter(final File in, URL baseURL) throws FileNotFoundException {
        u.p("loadPainter: " + in.getAbsolutePath() + " " + baseURL.toString());
        Thread.currentThread().setContextClassLoader(PainterUtil.class.getClassLoader());
        u.p("checking the class: " + MattePainter.class.getName());
        XMLDecoder dec = new XMLDecoder(new FileInputStream(in));
        
        dec.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception exception) {
                u.p(exception);
            }
        });
        Object obj = dec.readObject();
        setAAOn((Painter)obj);
        u.p("returning: " + obj);
        return (Painter)obj;
    }
    
    public static Painter loadPainter(URL url) throws IOException {
        u.p("loading: " + url);
        XMLDecoder dec = new XMLDecoder(url.openStream());
        
        dec.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception exception) {
                u.p(exception);
            }
        });
        Object obj = dec.readObject();
        u.p("object = " + obj);
        return (Painter)obj;
    }
    
    public static void setAAOn(Painter painter) {
        if(painter instanceof AbstractPainter) {
            ((AbstractPainter)painter).setAntialiasing(RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if(painter instanceof CompoundPainter) {
            Painter[] pt = ((CompoundPainter)painter).getPainters();
            for(Painter p : pt) {
                setAAOn(p);
            }
        }
    }
    
    static public void savePainterToFile(Painter compoundPainter, File file) throws IOException {
 	//System.setErr(null);
        u.p("writing out to: " + file.getCanonicalPath());
        setTransient(ImagePainter.class, "image");
        //setTransient(CompoundPainter.class,"antialiasing");
        setTransient(AbstractPainter.class,"antialiasing");
        setTransient(AbstractPainter.class,"renderingHints");
        //setPropertyDelegate();
        
        XMLEncoder e = new XMLEncoder(new FileOutputStream(file));
        //e.setPersistenceDelegate(AbstractPainter.class, new AbstractPainterDelegate());
        //e.setPersistenceDelegate(RenderingHints.class, new RenderingHintsDelegate());
        e.setPersistenceDelegate(GradientPaint.class, new GradientPaintDelegate());
        e.setPersistenceDelegate(Arc2D.Float.class, new Arc2DDelegate());
        e.setPersistenceDelegate(Arc2D.Double.class, new Arc2DDelegate());
        e.setPersistenceDelegate(CubicCurve2D.Float.class, new CubicCurve2DDelegate());
        e.setPersistenceDelegate(CubicCurve2D.Double.class, new CubicCurve2DDelegate());
        e.setPersistenceDelegate(Ellipse2D.Float.class, new Ellipse2DDelegate());
        e.setPersistenceDelegate(Ellipse2D.Double.class, new Ellipse2DDelegate());
        e.setPersistenceDelegate(Line2D.Float.class, new Line2DDelegate());
        e.setPersistenceDelegate(Line2D.Double.class, new Line2DDelegate());
        e.setPersistenceDelegate(Point2D.Float.class, new Point2DDelegate());
        e.setPersistenceDelegate(Point2D.Double.class, new Point2DDelegate());
        e.setPersistenceDelegate(QuadCurve2D.Float.class, new QuadCurve2DDelegate());
        e.setPersistenceDelegate(QuadCurve2D.Double.class, new QuadCurve2DDelegate());
        e.setPersistenceDelegate(Rectangle2D.Float.class, new Rectangle2DDelegate());
        e.setPersistenceDelegate(Rectangle2D.Double.class, new Rectangle2DDelegate());
        e.setPersistenceDelegate(RoundRectangle2D.Float.class, new RoundRectangle2DDelegate());
        e.setPersistenceDelegate(RoundRectangle2D.Double.class, new RoundRectangle2DDelegate());
        e.setPersistenceDelegate(Area.class, new AreaDelegate());
        e.setPersistenceDelegate(GeneralPath.class, new GeneralPathDelegate());
        e.setPersistenceDelegate(AffineTransform.class, new AffineTransformDelegate());
        e.setPersistenceDelegate(RadialGradientPaint.class, new RadialGradientPaintDelegate());
        e.setPersistenceDelegate(LinearGradientPaint.class, new LinearGradientPaintDelegate());
        e.setPersistenceDelegate(Insets.class, new InsetsDelegate());
        
        e.writeObject(compoundPainter);
        e.close();
    }
    
    //private static void setPropertyDelegate(Class clazz, String property, )
    private static void setTransient(Class clazz, String property) {
        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors =
                    info.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; ++i) {
                PropertyDescriptor pd = propertyDescriptors[i];
                if (pd.getName().equals(property)) {
                    pd.setValue("transient", Boolean.TRUE);
                    //u.p(pd.attributeNames());
                }
            }
        } catch (Exception ex) {
            //u.p(ex);
        }
    }
    
    
    public static final class RenderingHintsDelegate extends PersistenceDelegate {
        protected Expression instantiate(Object oldInstance, Encoder out) {
            //u.p("rh inst");
            // give it a constructor w/ null as the argument
            return new Expression(oldInstance, oldInstance.getClass(),
                    "new", new Object[] { null });
        }
        protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out) {
            //u.p("rh init ");
            RenderingHints rh = (RenderingHints)oldInstance;
            out.writeStatement(new Statement(oldInstance, "put",
                    new Object[] {RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON}));
            //u.p("done");
        }
    }
    
    public static final class AbstractPainterDelegate extends DefaultPersistenceDelegate {
        protected void initialize(Class type, Object oldInstance,
                Object newInstance, Encoder out) {
            System.out.println("ap delegate called");
            // Note, the "size" property will be set here.
            super.initialize(type, oldInstance,  newInstance, out);
            
            //AbstractPainter ap = (AbstractPainter)oldInstance;
            //RenderingHints rh = ap.getRenderingHints();
            //out.writeStatement(new Statement(oldInstance, "setRenderingHints", new Object[]{rh}));
        }
    }
    
    public static void savePainterToImage(JComponent testPanel, CompoundPainter compoundPainter, File file) throws IOException {
        BufferedImage img = new BufferedImage(testPanel.getWidth(),testPanel.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        setBGP(testPanel,compoundPainter);
        testPanel.paint(g);
        ImageIO.write(img,"png",file);
    }
    
    public static void setBGP(JComponent comp, Painter painter) {
        if(comp instanceof JXPanel) {
            ((JXPanel)comp).setBackgroundPainter(painter);
        }
        if(comp instanceof JXLabel) {
            ((JXLabel)comp).setBackgroundPainter(painter);
        }
        if(comp instanceof JXButton) {
            ((JXButton)comp).setBackgroundPainter(painter);
        }
    }
    public static void setFGP(JComponent comp, Painter painter) {
        if(comp instanceof JXPanel) {
            ((JXPanel)comp).setForegroundPainter(painter);
        }
        if(comp instanceof JXLabel) {
            ((JXLabel)comp).setForegroundPainter(painter);
        }
        if(comp instanceof JXButton) {
            ((JXButton)comp).setForegroundPainter(painter);
        }
    }

    public static Painter getFGP(JComponent comp) {
        if(comp instanceof JXPanel) {
            return ((JXPanel)comp).getForegroundPainter();
        }
        if(comp instanceof JXLabel) {
            return ((JXLabel)comp).getForegroundPainter();
        }
        if(comp instanceof JXButton) {
            return ((JXButton)comp).getForegroundPainter();
        }
        return null;
    }

    public static Painter getBGP(JComponent comp) {
        if(comp instanceof JXPanel) {
            return ((JXPanel)comp).getBackgroundPainter();
        }
        if(comp instanceof JXLabel) {
            return ((JXLabel)comp).getBackgroundPainter();
        }
        if(comp instanceof JXButton) {
            return ((JXButton)comp).getBackgroundPainter();
        }
        return null;
    }
}
