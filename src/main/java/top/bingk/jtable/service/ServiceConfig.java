package top.bingk.jtable.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public abstract class ServiceConfig extends JFinalConfig {

    protected Prop myProp = mergeProp(getProp(), PropKit.use("jfinal.properties"));

    private static Prop getProp() {
        Prop myProp = null;
        String jarWholePath = ServiceConfig.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        String jarPath = new File(jarWholePath).getParentFile().getAbsolutePath();
        File file = new File(jarPath + "/jfinal.properties");

        if (file.exists()) {
            myProp = new Prop(file);
        }
        return myProp;
    }

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(myProp.getBoolean("devMode", false));
    }

    @Override
    public void configRoute(Routes me) {

    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        addPlugin(plugins);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.addGlobalServiceInterceptor(new TxByMethodRegex("(.*save.*|.*update.*|.*delete.*)"));
    }

    @Override
    public void configHandler(Handlers handlers) {

    }

    public DruidPlugin createDruid() {
        DruidPlugin druidPlugin =
            new DruidPlugin(myProp.get("jdbcUrl"), myProp.get("userName"), myProp.get("password").trim());
        druidPlugin.setFilters(myProp.get("filters"));
        druidPlugin.setMaxActive(myProp.getInt("maxActive"));
        druidPlugin.setInitialSize(myProp.getInt("initialSize"));
        druidPlugin.setMaxWait(myProp.getInt("maxWait"));
        druidPlugin.setMinIdle(myProp.getInt("minIdle"));
        druidPlugin.setTimeBetweenEvictionRunsMillis(myProp.getInt("timeBetweenEvictionRunsMillis"));
        druidPlugin.setMinEvictableIdleTimeMillis(myProp.getInt("minEvictableIdleTimeMillis"));
        druidPlugin.setValidationQuery(myProp.get("validationQuery"));
        druidPlugin.setTestWhileIdle(myProp.getBoolean("testWhileIdle"));
        druidPlugin.setTestOnBorrow(myProp.getBoolean("testOnBorrow"));
        druidPlugin.setTestOnReturn(myProp.getBoolean("testOnReturn"));
        druidPlugin.setRemoveAbandoned(myProp.getBoolean("removeAbandoned"));
        druidPlugin.setRemoveAbandonedTimeoutMillis(myProp.getInt("removeAbandonedTimeout"));
        druidPlugin.setLogAbandoned(myProp.getBoolean("logAbandoned"));
        return druidPlugin;
    }

    public abstract void addPlugin(Plugins plugins);

    public static Prop mergeProp(Prop... props) {
        Prop result = null;
        List<Prop> propListWithoutNull = new ArrayList<Prop>();
        for (Prop prop1 : props) {
            if (prop1 != null) {
                propListWithoutNull.add(prop1);
            }
        }

        if (propListWithoutNull.size() == 0) {
            return result;
        }

        if (propListWithoutNull.size() == 1) {
            return propListWithoutNull.get(0);
        }

        for (Prop prop : propListWithoutNull) {
            if (prop == null) {
                continue;
            }
            if (result == null) {
                result = prop;
                continue;
            }
            Properties temp = prop.getProperties();
            for (Entry<Object, Object> pros : temp.entrySet()) {
                if (StrKit.isBlank(result.get(String.valueOf(pros.getKey())))) {
                    result.getProperties().setProperty(String.valueOf(pros.getKey()), String.valueOf(pros.getValue()));
                }
            }
        }
        return result;
    }

}
