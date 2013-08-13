package parser;

import iitb.shared.EntryWithScore;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import catalog.QuantityCatalog;
import catalog.Unit;
import edu.stanford.nlp.parser.lexparser.Lexicon;

public class CFGParser4Text extends CFGParser4Header {
	public CFGParser4Text(Element options) throws IOException,
			ParserConfigurationException, SAXException {
		super(options);
		// TODO Auto-generated constructor stub
	}
	public static String QuantityToken="qqqq";
	public CFGParser4Text(Element options, QuantityCatalog quantMatcher)
			throws IOException, ParserConfigurationException, SAXException {
		super(options, quantMatcher);
	}
	// put the grammar before BU.
	public static String textGrammar=
		"ROOT ::- ROOT_ "+Lexicon.BOUNDARY_TAG + " 1f" + "\n" +
        "ROOT_ ::- Junk Q_U 1f" + "\n" +
        "ROOT_ ::- Junk 1f" + "\n" +
        "ROOT_ ::- Junk_QU Junk 1f" + "\n" +
        "ROOT_ ::- Q_U Junk 1f" + "\n" +
        "ROOT_ ::- Q_U 1f" + "\n" +

        "Junk_QU ::= Junk Q_U 1f"+ "\n" +
        "Junk ::= Junk W 1f"+ "\n" +
        "Junk ::= W 1f"+ "\n" +
        
        "Q_U ::- Q U 1f" +    "\n" + //Quantity followed by a unit.
        "Q_U ::- U Q 1f" +    "\n" +                // a units followed by a quantity e.g. "$500"
        "Q_U ::- Q 1f" + "\n" +                    // unitless and multiplier-less quantity e.g. Population of India is 1,200,000
        //Assuming Q is tag standing for a quantity, expressed either in words or numbers, which has been recognized and normalized
        "U ::= BU 1f"+ "\n";
       
	@Override
	protected String getGrammar() {
		return textGrammar + basicUnitGrammar;
	}
	public static void main(String args[]) throws Exception {
		Vector<UnitObject> featureList = new Vector();
		List<EntryWithScore<Unit>> unitsR = new CFGParser4Text(null).parseHeader("$" + QuantityToken + " million",	null
				//new short[][]{{(short) Tags.W.ordinal()},{(short) Tags.SU.ordinal()},{(short) Tags.PER.ordinal()},{(short) Tags.SU.ordinal()}
				//,{(short) Tags.SU.ordinal()},{(short) Tags.PER.ordinal()},{(short) Tags.SU.ordinal()}}
				,1);
		if (unitsR != null) {
			for (EntryWithScore<Unit> unit : unitsR) {
				System.out.println(unit.getKey().getName()+ " " +unit.getScore());
			}
		}
		
	}
}