
import jade.Boot;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.ArrayList;
import java.util.Iterator;


public class AgenteMovil extends Agent {
    // Ejecutado por unica vez en la creacion
    private Location origen;
    private Integer indexContainer = 0;
    private ArrayList<ContainerID> containersIds = new ArrayList<ContainerID>();

    public void setup() {
        origen = here();
        System.out.println("\n\nHola, agente con nombre local " + getLocalName());
        System.out.println("Y nombre completo... " + getName());
        System.out.println("Y en location " + origen.getID() + "\n\n");
        addBehaviour(fillContainersList());
        addBehaviour(new MoveToContainers(this));
    }

    class MoveToContainers extends SimpleBehaviour {

        boolean finalize = false;
        AgenteMovil a;

        public MoveToContainers(AgenteMovil a) {
            super(a);
            this.a = a;
        }

        @Override
        public void action() {
            try {
                a.indexContainer = a.indexContainer + 1;
                if (a.indexContainer >= a.containersIds.size()) {
                    System.out.println("\nFin de recorrido");
                    return;
                }
                doMove(a.containersIds.get(a.indexContainer));
            } catch (Exception e) {
                System.out.println("\n\n\nNo fue posible migrar el agente\n\n\n");
            }
            finalize = true;
        }

        @Override
        public boolean done() {
            return finalize;
        }
    }


    private Behaviour fillContainersList() {
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
        Behaviour b = new AchieveREInitiator(this, request) {

            protected void handleInform(ACLMessage inform) {
                jade.util.leap.List platformContainers;
                try {
                    Result r = (Result) getContentManager().extractContent(inform);
                    platformContainers = (jade.util.leap.List) r.getValue();
                    Iterator iter = platformContainers.iterator();
                    System.out.println("Contenedores en la pataforma");
                    while (iter.hasNext()) {
                        containersIds.add(((ContainerID) iter.next()));
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            protected void handleFailure(ACLMessage failure) {
                System.out.println("Error while getting container list");
            }

        };
        return b;
    }

    protected void captureAndReportSysInfo() {

    }

    protected void afterMove() {
        System.out.println("\n\nHola, agente migrado con nombre local " + getLocalName());
        captureAndReportSysInfo();

    }

        /* Me devuleve todos los agentes
        AMSAgentDescription [] agents = null;

        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults ( new Long(-1) );
            agents = AMSService.search( this, new AMSAgentDescription (), c );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for (int i=0; i<agents.length;i++){
            AID agentID = agents[i].getName();
            System.out.println(agentID.getLocalName());
        }
        */
    // Para migrar el agente

        /*try {
            ContainerID destino = new ContainerID("Main-Container", null);
            System.out.println("Migrando el agente a " + destino.getID());
            doMove(destino);
        } catch (Exception e) {
            System.out.println("\n\n\nNo fue posible migrar el agente\n\n\n");
        }*/


}

