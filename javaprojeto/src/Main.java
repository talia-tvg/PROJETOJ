import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimuladorArduino {

    // Limites de operação
    private static final float LIMITE_TEMPERATURA = 40.0f; // Limite para alerta de temperatura
    private static final int VALOR_MINIMO_UMIDADE = 300;   // Valor mínimo para simulação de umidade
    private static final int VALOR_MAXIMO_UMIDADE = 700;   // Valor máximo para simulação de umidade

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Simulador de dados do Arduino
        executor.execute(() -> {
            Random random = new Random();
            while (true) {
                try {
                    // Simular dados
                    float temperatura = 20 + random.nextFloat() * 15; // Temperatura entre 20°C e 35°C
                    int leituraUmidade = VALOR_MINIMO_UMIDADE + random.nextInt(VALOR_MAXIMO_UMIDADE - VALOR_MINIMO_UMIDADE + 1); // Simular leitura analógica de umidade
                    int leituraLuminosidade = random.nextInt(1024); // Simular leitura analógica de luminosidade

                    // Conversão da umidade para porcentagem
                    int umidade = (int) map(leituraUmidade, VALOR_MINIMO_UMIDADE, VALOR_MAXIMO_UMIDADE, 0, 100);
                    umidade = constrain(umidade, 0, 100);

                    // Conversão da luminosidade para lux
                    float luminosidadeLux = (1023.0f - leituraLuminosidade) * (500.0f / 1023.0f);

                    // Simular atraso de leitura
                    Thread.sleep(500); // Atraso de 0,5 segundos para simular comportamento do Arduino

                    // Exibir dados simulados no console
                    System.out.printf("Temperatura: %.1f °C\n", temperatura);
                    System.out.println("Umidade: " + umidade + " %");
                    System.out.printf("Luminosidade: %.0f lux\n", luminosidadeLux);

                    // Simular comportamento do LED de alerta
                    if (temperatura > LIMITE_TEMPERATURA) {
                        System.out.println("LED ALERTA: ACESO");
                    } else {
                        System.out.println("LED ALERTA: APAGADO");
                    }

                    System.out.println("----------------------------");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Fechar executor ao encerrar
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }

    // Função para mapear valores (similar ao map() do Arduino)
    private static float map(float valor, float entradaMin, float entradaMax, float saidaMin, float saidaMax) {
        return ((valor - entradaMin) / (entradaMax - entradaMin)) * (saidaMax - saidaMin) + saidaMin;
    }

    // Função para limitar valores (similar ao constrain() do Arduino)
    private static int constrain(int valor, int min, int max) {
        return Math.max(min, Math.min(max, valor));
    }
}
