/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcos
 */
public final class BusquedaUtil {
    
    private static final Logger log = LoggerFactory.getLogger(BusquedaUtil.class);
    
    private static final Map<String, String> mapaStopWords = new HashMap<>();
    
    public BusquedaUtil()
    {
        iniciarMapaStopWords();
    }
           
    
    public void iniciarMapaStopWords()
    {
        mapaStopWords.put("a", "a");
        mapaStopWords.put("acá", "acá");
        mapaStopWords.put("ahí", "ahí");
        mapaStopWords.put("ajena", "ajena");
        mapaStopWords.put("ajeno", "ajeno");
        mapaStopWords.put("ajenas", "ajenas");
        mapaStopWords.put("ajenos", "ajenos");
        mapaStopWords.put("al", "al");
        mapaStopWords.put("algo", "algo");
        mapaStopWords.put("algún", "algún");
        mapaStopWords.put("alguna", "alguna");
        mapaStopWords.put("alguno", "alguno");
        mapaStopWords.put("algunas", "algunas");
        mapaStopWords.put("algunos", "algunos");
        mapaStopWords.put("allá", "allá");
        mapaStopWords.put("allí", "allí");
        mapaStopWords.put("ambos", "ambos");
        mapaStopWords.put("ante", "ante");
        mapaStopWords.put("antes", "antes");
        mapaStopWords.put("aquel", "aquel");
        mapaStopWords.put("aquella", "aquella");
        mapaStopWords.put("aquello", "aquello");
        mapaStopWords.put("aquellas", "aquellas");
        mapaStopWords.put("aquellos", "aquellos");
        mapaStopWords.put("aquí", "aquí");
        mapaStopWords.put("arriba", "arriba");
        mapaStopWords.put("así", "así");
        mapaStopWords.put("atrás", "atrás");
        mapaStopWords.put("aun", "aun");
        mapaStopWords.put("aunque", "aunque");
        mapaStopWords.put("bajo", "bajo");
        mapaStopWords.put("bastante", "bastante");
        mapaStopWords.put("bien", "bien");
        mapaStopWords.put("cabe", "cabe");
        mapaStopWords.put("cada", "cada");
        mapaStopWords.put("casi", "casi");
        mapaStopWords.put("cierto", "cierto");
        mapaStopWords.put("cierta", "cierta");
        mapaStopWords.put("ciertos", "ciertos");
        mapaStopWords.put("ciertas", "ciertas");
        mapaStopWords.put("como", "como");
        mapaStopWords.put("con", "con");
        mapaStopWords.put("conmigo", "conmigo");
        mapaStopWords.put("conseguimos", "conseguimos");
        mapaStopWords.put("conseguir", "conseguir");
        mapaStopWords.put("consigo", "consigo");
        mapaStopWords.put("consigue", "consigue");
        mapaStopWords.put("consiguen", "consiguen");
        mapaStopWords.put("consigues", "consigues");
        mapaStopWords.put("contigo", "contigo");
        mapaStopWords.put("contra", "contra");
        mapaStopWords.put("cual", "cual");
        mapaStopWords.put("cuales", "cuales");
        mapaStopWords.put("cualquier", "cualquier");
        mapaStopWords.put("cualquiera", "cualquiera");
        mapaStopWords.put("cualquieras", "cualquieras");
        mapaStopWords.put("cuan", "cuan");
        mapaStopWords.put("cuando", "cuando");
        mapaStopWords.put("cuanto", "cuanto");
        mapaStopWords.put("cuanta", "cuanta");
        mapaStopWords.put("cuantos", "cuantos");
        mapaStopWords.put("cuantas", "cuantas");
        mapaStopWords.put("de", "de");
        mapaStopWords.put("dejar", "dejar");
        mapaStopWords.put("del", "del");
        mapaStopWords.put("demás", "demás");
        mapaStopWords.put("demasiada", "demasiada");
        mapaStopWords.put("demasiado", "demasiado");
        mapaStopWords.put("demasiadas", "demasiadas");
        mapaStopWords.put("demasiados", "demasiados");
        mapaStopWords.put("dentro", "dentro");
        mapaStopWords.put("desde", "desde");
        mapaStopWords.put("donde", "donde");
        mapaStopWords.put("dos", "dos");
        mapaStopWords.put("el", "el");
        mapaStopWords.put("él", "él");
        mapaStopWords.put("ella", "ella");
        mapaStopWords.put("ellas", "ellas");
        mapaStopWords.put("ellos", "ellos");
        mapaStopWords.put("empleáis", "empleáis");
        mapaStopWords.put("emplean", "emplean");
        mapaStopWords.put("emplear", "emplear");
        mapaStopWords.put("empleas", "empleas");
        mapaStopWords.put("empleo", "empleo");
        mapaStopWords.put("en", "en");
        mapaStopWords.put("encima", "encima");
        mapaStopWords.put("entonces", "entonces");
        mapaStopWords.put("entre", "entre");
        mapaStopWords.put("era", "era");
        mapaStopWords.put("eras", "eras");
        mapaStopWords.put("eramos", "eramos");
        mapaStopWords.put("eran", "eran");
        mapaStopWords.put("eres", "eres");
        mapaStopWords.put("es", "es");
        mapaStopWords.put("esa", "esa");
        mapaStopWords.put("ese", "ese");
        mapaStopWords.put("esas", "esas");
        mapaStopWords.put("esos", "esos");
        mapaStopWords.put("eso", "eso");
        mapaStopWords.put("esta", "esta");
        mapaStopWords.put("estas", "estas");
        mapaStopWords.put("estaba", "estaba");
        mapaStopWords.put("estado", "estado");
        mapaStopWords.put("estáis", "estáis");
        mapaStopWords.put("estamos", "estamos");
        mapaStopWords.put("están", "están");
        mapaStopWords.put("estar", "estar");
        mapaStopWords.put("este", "este");
        mapaStopWords.put("esto", "esto");
        mapaStopWords.put("estos", "estos");
        mapaStopWords.put("estoy", "estoy");
        mapaStopWords.put("etc", "etc");
        mapaStopWords.put("fin", "fin");
        mapaStopWords.put("fue", "fue");
        mapaStopWords.put("fueron", "fueron");
        mapaStopWords.put("fui", "fui");
        mapaStopWords.put("fuimos", "fuimos");
        mapaStopWords.put("gueno", "gueno");
        mapaStopWords.put("ha", "ha");
        mapaStopWords.put("hace", "hace");
        mapaStopWords.put("haces", "haces");
        mapaStopWords.put("hacéis", "hacéis");
        mapaStopWords.put("hacemos", "hacemos");
        mapaStopWords.put("hacen", "hacen");
        mapaStopWords.put("hacer", "hacer");
        mapaStopWords.put("hacia", "hacia");
        mapaStopWords.put("hago", "hago");
        mapaStopWords.put("hasta", "hasta");
        mapaStopWords.put("incluso", "incluso");
        mapaStopWords.put("intenta", "intenta");
        mapaStopWords.put("intentas", "intentas");
        mapaStopWords.put("intentáis", "intentáis");
        mapaStopWords.put("intentamos", "intentamos");
        mapaStopWords.put("intentan", "intentan");
        mapaStopWords.put("intentar", "intentar");
        mapaStopWords.put("intento", "intento");
        mapaStopWords.put("ir", "ir");
        mapaStopWords.put("jamás", "jamás");
        mapaStopWords.put("junto", "junto");
        mapaStopWords.put("juntos", "juntos");
        mapaStopWords.put("la", "la");
        mapaStopWords.put("lo", "lo");
        mapaStopWords.put("las", "las");
        mapaStopWords.put("los", "los");
        mapaStopWords.put("largo", "largo");
        mapaStopWords.put("más", "más");
        mapaStopWords.put("me", "me");
        mapaStopWords.put("menos", "menos");
        mapaStopWords.put("mi", "mi");
        mapaStopWords.put("mis", "mis");
        mapaStopWords.put("mía", "mía");
        mapaStopWords.put("mías", "mías");
        mapaStopWords.put("mientras", "mientras");
        mapaStopWords.put("mío", "mío");
        mapaStopWords.put("míos", "míos");
        mapaStopWords.put("misma", "misma");
        mapaStopWords.put("mismo", "mismo");
        mapaStopWords.put("mismas", "mismas");
        mapaStopWords.put("mismos", "mismos");
        mapaStopWords.put("modo", "modo");
        mapaStopWords.put("mucha", "mucha");
        mapaStopWords.put("muchas", "muchas");
        mapaStopWords.put("muchísima", "muchísima");
        mapaStopWords.put("muchísimo", "muchísimo");
        mapaStopWords.put("muchísimas", "muchísimas");
        mapaStopWords.put("muchísimos", "muchísimos");
        mapaStopWords.put("mucho", "mucho");
        mapaStopWords.put("muchos", "muchos");
        mapaStopWords.put("muy", "muy");
        mapaStopWords.put("nada", "nada");
        mapaStopWords.put("ni", "ni");
        mapaStopWords.put("ningún", "ningún");
        mapaStopWords.put("ninguna", "ninguna");
        mapaStopWords.put("ninguno", "ninguno");
        mapaStopWords.put("ningunas", "ningunas");
        mapaStopWords.put("ningunos", "ningunos");
        mapaStopWords.put("no", "no");
        mapaStopWords.put("nos", "nos");
        mapaStopWords.put("nosotras", "nosotras");
        mapaStopWords.put("nosotros", "nosotros");
        mapaStopWords.put("nuestra", "nuestra");
        mapaStopWords.put("nuestro", "nuestro");
        mapaStopWords.put("nuestras", "nuestras");
        mapaStopWords.put("nuestros", "nuestros");
        mapaStopWords.put("nunca", "nunca");
        mapaStopWords.put("os", "os");
        mapaStopWords.put("otra", "otra");
        mapaStopWords.put("otro", "otro");
        mapaStopWords.put("otras", "otras");
        mapaStopWords.put("otros", "otros");
        mapaStopWords.put("para", "para");
        mapaStopWords.put("parecer", "parecer");
        mapaStopWords.put("pero", "pero");
        mapaStopWords.put("poca", "poca");
        mapaStopWords.put("poco", "poco");
        mapaStopWords.put("pocas", "pocas");
        mapaStopWords.put("pocos", "pocos");
        mapaStopWords.put("podéis", "podéis");
        mapaStopWords.put("podemos", "podemos");
        mapaStopWords.put("poder", "poder");
        mapaStopWords.put("podría", "podría");
        mapaStopWords.put("podrías", "podrías");
        mapaStopWords.put("podríais", "podríais");
        mapaStopWords.put("podríamos", "podríamos");
        mapaStopWords.put("podrían", "podrían");
        mapaStopWords.put("por", "por");
        mapaStopWords.put("porque", "porque");
        mapaStopWords.put("primero", "primero");
        mapaStopWords.put("puede", "puede");
        mapaStopWords.put("pueden", "pueden");
        mapaStopWords.put("puedo", "puedo");
        mapaStopWords.put("pues", "pues");
        mapaStopWords.put("que", "que");
        mapaStopWords.put("qué", "qué");
        mapaStopWords.put("querer", "querer");
        mapaStopWords.put("quién", "quién");
        mapaStopWords.put("quienes", "quienes");
        mapaStopWords.put("quiénes", "quiénes");
        mapaStopWords.put("quienesquiera", "quienesquiera");
        mapaStopWords.put("quienquiera", "quienquiera");
        mapaStopWords.put("quizá", "quizá");
        mapaStopWords.put("quizás", "quizás");
        mapaStopWords.put("sabe", "sabe");
        mapaStopWords.put("sabes", "sabes");
        mapaStopWords.put("saben", "saben");
        mapaStopWords.put("sabéis", "sabéis");
        mapaStopWords.put("sabemos", "sabemos");
        mapaStopWords.put("saber", "saber");
        mapaStopWords.put("se", "se");
        mapaStopWords.put("según", "según");
        mapaStopWords.put("ser", "ser");
        mapaStopWords.put("si", "si");
        mapaStopWords.put("sí", "sí");
        mapaStopWords.put("siempre", "siempre");
        mapaStopWords.put("siendo", "siendo");
        mapaStopWords.put("sin", "sin");
        mapaStopWords.put("sino", "sino");
        mapaStopWords.put("so", "so");
        mapaStopWords.put("sobre", "sobre");
        mapaStopWords.put("sois", "sois");
        mapaStopWords.put("solamente", "solamente");
        mapaStopWords.put("solo", "solo");
        mapaStopWords.put("sólo", "sólo");
        mapaStopWords.put("somos", "somos");
        mapaStopWords.put("soy", "soy");
        mapaStopWords.put("sr", "sr");
        mapaStopWords.put("sra", "sra");
        mapaStopWords.put("sres", "sres");
        mapaStopWords.put("sta", "sta");
        mapaStopWords.put("su", "su");
        mapaStopWords.put("sus", "sus");
        mapaStopWords.put("suya", "suya");
        mapaStopWords.put("suyo", "suyo");
        mapaStopWords.put("suyas", "suyas");
        mapaStopWords.put("suyos", "suyos");
        mapaStopWords.put("tal", "tal");
        mapaStopWords.put("tales", "tales");
        mapaStopWords.put("también", "también");
        mapaStopWords.put("tampoco", "tampoco");
        mapaStopWords.put("tan", "tan");
        mapaStopWords.put("tanta", "tanta");
        mapaStopWords.put("tanto", "tanto");
        mapaStopWords.put("tantas", "tantas");
        mapaStopWords.put("tantos", "tantos");
        mapaStopWords.put("te", "te");
        mapaStopWords.put("tenéis", "tenéis");
        mapaStopWords.put("tenemos", "tenemos");
        mapaStopWords.put("tener", "tener");
        mapaStopWords.put("tengo", "tengo");
        mapaStopWords.put("ti", "ti");
        mapaStopWords.put("tiempo", "tiempo");
        mapaStopWords.put("tiene", "tiene");
        mapaStopWords.put("tienen", "tienen");
        mapaStopWords.put("toda", "toda");
        mapaStopWords.put("todo", "todo");
        mapaStopWords.put("todas", "todas");
        mapaStopWords.put("todos", "todos");
        mapaStopWords.put("tomar", "tomar");
        mapaStopWords.put("trabaja", "trabaja");
        mapaStopWords.put("trabajo", "trabajo");
        mapaStopWords.put("trabajáis", "trabajáis");
        mapaStopWords.put("trabajamos", "trabajamos");
        mapaStopWords.put("trabajan", "trabajan");
        mapaStopWords.put("trabajar", "trabajar");
        mapaStopWords.put("trabajas", "trabajas");
        mapaStopWords.put("tras", "tras");
        mapaStopWords.put("tú", "tú");
        mapaStopWords.put("tu", "tu");
        mapaStopWords.put("tus", "tus");
        mapaStopWords.put("tuya", "tuya");
        mapaStopWords.put("tuyo", "tuyo");
        mapaStopWords.put("tuyas", "tuyas");
        mapaStopWords.put("tuyos", "tuyos");
        mapaStopWords.put("último", "último");
        mapaStopWords.put("ultimo", "ultimo");
        mapaStopWords.put("un", "un");
        mapaStopWords.put("una", "una");
        mapaStopWords.put("uno", "uno");
        mapaStopWords.put("unas", "unas");
        mapaStopWords.put("unos", "unos");
        mapaStopWords.put("usa", "usa");
        mapaStopWords.put("usas", "usas");
        mapaStopWords.put("usáis", "usáis");
        mapaStopWords.put("usamos", "usamos");
        mapaStopWords.put("usan", "usan");
        mapaStopWords.put("usar", "usar");
        mapaStopWords.put("uso", "uso");
        mapaStopWords.put("usted", "usted");
        mapaStopWords.put("ustedes", "ustedes");
        mapaStopWords.put("va", "va");
        mapaStopWords.put("van", "van");
        mapaStopWords.put("vais", "vais");
        mapaStopWords.put("valor", "valor");
        mapaStopWords.put("vamos", "vamos");
        mapaStopWords.put("varias", "varias");
        mapaStopWords.put("varios", "varios");
        mapaStopWords.put("vaya", "vaya");
        mapaStopWords.put("verdadera", "verdadera");
        mapaStopWords.put("vosotras", "vosotras");
        mapaStopWords.put("vosotros", "vosotros");
        mapaStopWords.put("voy", "voy");
        mapaStopWords.put("vuestra", "vuestra");
        mapaStopWords.put("vuestro", "vuestro");
        mapaStopWords.put("vuestras", "vuestras");
        mapaStopWords.put("vuestros", "vuestros");
        mapaStopWords.put("y", "y");
        mapaStopWords.put("ya", "ya");
        mapaStopWords.put("yo", "yo");
    }
    
    public String eliminacionStopWords(String[] palabrasBusqueda)
    {
        String resp = null;             
               
        for(String palabra : palabrasBusqueda)
        {
            if(!mapaStopWords.containsKey(palabra))
            {
                resp = palabra + " ";
                log.info("palabra clave detectada: {}", palabra);
            }
        }
        
        if(resp == null)
            log.info("No existen palabras claves en la búsqueda");
        else
           resp = resp.trim(); 
        
        return resp;
    }
}
