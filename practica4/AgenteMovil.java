
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.behaviours.SimpleBehaviour;


public class AgenteMovil extends Agent {
    // Ejecutado por unica vez en la creacion
    private Location origen;

    public void setup() {
        Location origen = here();
        System.out.println("\n\nHola, agente con nombre local " + getLocalName());
        System.out.println("Y nombre completo... " + getName());
        System.out.println("Y en location " + origen.getID() + "\n\n");
        addBehaviour(new B1(this));
    }

    class B1 extends SimpleBehaviour{

        boolean finalize = false;
        public B1(Agent a){
            super(a);
        }
        @Override
        public void action() {
            try {
                ContainerID destino = new ContainerID("Main-Container", null);
                System.out.println("Migrando el agente a " + destino.getID());
                System.out.println("\nOrigen: ");
                finalize = true;
                doMove(destino);
            } catch (Exception e) {
                System.out.println("\n\n\nNo fue posible migrar el agente\n\n\n");
            }
        }

        @Override
        public boolean done() {
            return finalize;
        }
    }

    // Ejecutado al llegar a un contenedor como resultado de una migracion
    protected void afterMove() {
        System.out.println("\n\nHola, agente migrado con nombre local " + getLocalName());
        System.out.println("\nOrigen: ");
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

