import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.util.Iterator;


public class Agente2 extends Agent {

    String origen;
    public void setup() {
        System.out.println("\n\nHola, agente con nombre local " + getLocalName());
        origen = here().getName();
        this.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
        this.getContentManager().registerOntology(JADEManagementOntology.getInstance());

        QueryPlatformLocationsAction query = new QueryPlatformLocationsAction();
        Action action = new Action(getAMS(), query);
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            getContentManager().fillContent(request, action);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
        addBehaviour(new AchieveREInitiator(this, request) {
            protected void handleInform(ACLMessage inform) {
                jade.util.leap.List platformContainers;
                try {
                    Result r = (Result) getContentManager().extractContent(inform);
                    platformContainers = (jade.util.leap.List) r.getValue();
                    Iterator iter = platformContainers.iterator();
                    System.out.println("Contenedores en la pataforma");
                    while (iter.hasNext()) {
                        System.out.println(((ContainerID)iter.next()).getName());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            protected void handleFailure(ACLMessage failure) {
                System.out.println("Error while getting container list");
            }
        });
    }
}

