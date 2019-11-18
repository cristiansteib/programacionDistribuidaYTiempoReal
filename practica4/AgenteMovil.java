
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

import javax.print.attribute.standard.OrientationRequested;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;


public class AgenteMovil extends Agent {
    // Ejecutado por unica vez en la creacion
    private Location origen;
    private Integer indexContainer = 0;
    private ArrayList<ContainerID> containersIds = new ArrayList<ContainerID>();
    private ArrayList<SystemInfo> systemInfo = new ArrayList<SystemInfo>();
    private Long  startTime = System.currentTimeMillis();

    public void setup() {
        origen = here();
        System.out.println("\n\nHola, agente con nombre local " + getLocalName());
        System.out.println("Y nombre completo... " + getName());
        System.out.println("Y en location " + origen.getID() + "\n\n");
        Behaviour b = fillContainersList();
        addBehaviour(b);
    }


    public void addSystemInfo(SystemInfo s){
        systemInfo.add(s);
    }

    public void printSystemInfo(){
        for (SystemInfo sysInfo: systemInfo){
            System.out.println(sysInfo.toString());
        }
    }



    class MoveToContainers extends SimpleBehaviour {

        boolean finalize = false;
        AgenteMovil a;

        public MoveToContainers(AgenteMovil a) {
            super(a);
            this.a = a;
        }

        void gatherSystemInfo() {
            SystemInfo s = new SystemInfo();
            try {
                OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
                s.hostname = InetAddress.getLocalHost().getHostName();
                s.memoryAvailable = Runtime.getRuntime().freeMemory();
                s.loadAverage = os.getSystemLoadAverage();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            addSystemInfo(s);
        }

        @Override
        public void action() {
            try {
                gatherSystemInfo();
                a.indexContainer = a.indexContainer + 1;
                if (a.indexContainer >= a.containersIds.size()) {
                    a.printSystemInfo();
                    System.out.println("\nFin de recorrido, tiempo total: " + (System.currentTimeMillis() -  a.startTime) + "ms");
                    finalize = true;
                    return;
                }
                doMove(a.containersIds.get(a.indexContainer));
                this.action();
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
                    System.out.println(platformContainers.size());
                    while (iter.hasNext()) {
                        containersIds.add(((ContainerID) iter.next()));
                    }
                    addBehaviour(new MoveToContainers(AgenteMovil.this));
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

    protected void afterMove() {
        System.out.println("\n\nHola, agente migrado con nombre local " + getLocalName());
    }
}

