# Módulo 02: Ingeniería de Prompts con GPT-5.2

## Tabla de Contenidos

- [Lo que aprenderás](../../../02-prompt-engineering)
- [Prerrequisitos](../../../02-prompt-engineering)
- [Entendiendo la Ingeniería de Prompts](../../../02-prompt-engineering)
- [Fundamentos de Ingeniería de Prompts](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Cadena de Pensamiento](../../../02-prompt-engineering)
  - [Prompting Basado en Roles](../../../02-prompt-engineering)
  - [Plantillas de Prompts](../../../02-prompt-engineering)
- [Patrones Avanzados](../../../02-prompt-engineering)
- [Uso de Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Pantalla de la Aplicación](../../../02-prompt-engineering)
- [Explorando los Patrones](../../../02-prompt-engineering)
  - [Baja vs Alta Disposición](../../../02-prompt-engineering)
  - [Ejecución de Tareas (Preámbulos de Herramientas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análisis Estructurado](../../../02-prompt-engineering)
  - [Chat Multiturno](../../../02-prompt-engineering)
  - [Razonamiento Paso a Paso](../../../02-prompt-engineering)
  - [Salida Restringida](../../../02-prompt-engineering)
- [Lo que Realmente Estás Aprendiendo](../../../02-prompt-engineering)
- [Próximos Pasos](../../../02-prompt-engineering)

## Lo que aprenderás

<img src="../../../translated_images/es/what-youll-learn.c68269ac048503b2.webp" alt="Lo que aprenderás" width="800"/>

En el módulo anterior, viste cómo la memoria habilita la IA conversacional y usaste los Modelos de GitHub para interacciones básicas. Ahora nos enfocaremos en cómo haces preguntas — los prompts en sí — usando GPT-5.2 de Azure OpenAI. La forma en que estructuras tus prompts afecta dramáticamente la calidad de las respuestas que obtienes. Empezamos con una revisión de las técnicas fundamentales de prompting, luego avanzamos a ocho patrones avanzados que aprovechan al máximo las capacidades de GPT-5.2.

Usaremos GPT-5.2 porque introduce control de razonamiento — puedes indicarle al modelo cuánto debe pensar antes de responder. Esto hace que las diferentes estrategias de prompting sean más evidentes y te ayuda a entender cuándo usar cada enfoque. También nos beneficiaremos de que Azure tiene menos límites de tasa para GPT-5.2 comparado con los Modelos de GitHub.

## Prerrequisitos

- Haber completado el Módulo 01 (recursos Azure OpenAI desplegados)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado por `azd up` en el Módulo 01)

> **Nota:** Si no has completado el Módulo 01, sigue primero las instrucciones de despliegue ahí.

## Entendiendo la Ingeniería de Prompts

<img src="../../../translated_images/es/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="¿Qué es la ingeniería de prompts?" width="800"/>

La ingeniería de prompts se trata de diseñar texto de entrada que consistentemente te dé los resultados que necesitas. No es solo hacer preguntas — es estructurar solicitudes para que el modelo entienda exactamente qué quieres y cómo entregarlo.

Piénsalo como dar instrucciones a un colega. "Arregla el bug" es vago. "Arregla la excepción de puntero nulo en UserService.java línea 45 añadiendo una comprobación de nulidad" es específico. Los modelos de lenguaje funcionan igual — la especificidad y la estructura importan.

<img src="../../../translated_images/es/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Cómo encaja LangChain4j" width="800"/>

LangChain4j provee la infraestructura — conexiones de modelos, memoria, y tipos de mensajes — mientras que los patrones de prompt son solo texto cuidadosamente estructurado que envías a través de esa infraestructura. Los bloques clave son `SystemMessage` (que establece el comportamiento y rol de la IA) y `UserMessage` (que lleva tu petición real).

## Fundamentos de Ingeniería de Prompts

<img src="../../../translated_images/es/five-patterns-overview.160f35045ffd2a94.webp" alt="Resumen de cinco patrones de ingeniería de prompts" width="800"/>

Antes de profundizar en los patrones avanzados de este módulo, hagamos una revisión de cinco técnicas fundamentales de prompting. Son los bloques constructores que todo ingeniero de prompts debe conocer. Si ya trabajaste con el [módulo de inicio rápido](../00-quick-start/README.md#2-prompt-patterns), los viste en acción — aquí está el marco conceptual detrás de ellos.

### Prompting Zero-Shot

El enfoque más simple: darle al modelo una instrucción directa sin ejemplos. El modelo se basa completamente en su entrenamiento para entender y ejecutar la tarea. Funciona bien para solicitudes sencillas donde el comportamiento esperado es obvio.

<img src="../../../translated_images/es/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instrucción directa sin ejemplos — el modelo infiere la tarea solo con la instrucción*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respuesta: "Positivo"
```

**Cuándo usar:** Clasificaciones simples, preguntas directas, traducciones, o cualquier tarea que el modelo pueda manejar sin guía adicional.

### Prompting Few-Shot

Proporciona ejemplos que demuestran el patrón que quieres que el modelo siga. El modelo aprende el formato esperado entrada-salida con tus ejemplos y lo aplica a nuevas entradas. Esto mejora dramáticamente la consistencia en tareas donde el formato o comportamiento deseado no es obvio.

<img src="../../../translated_images/es/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Aprendiendo de ejemplos — el modelo identifica el patrón y lo aplica a nuevas entradas*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Cuándo usar:** Clasificaciones personalizadas, formato consistente, tareas de dominio específico, o cuando los resultados zero-shot son inconsistentes.

### Cadena de Pensamiento

Pide al modelo que muestre su razonamiento paso a paso. En lugar de saltar directamente a una respuesta, el modelo descompone el problema y trabaja cada parte explícitamente. Esto mejora la precisión en matemáticas, lógica y razonamiento multi-paso.

<img src="../../../translated_images/es/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting Cadena de Pensamiento" width="800"/>

*Razonamiento paso a paso — descomponer problemas complejos en pasos lógicos explícitos*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// El modelo muestra: 15 - 8 = 7, luego 7 + 12 = 19 manzanas
```

**Cuándo usar:** Problemas matemáticos, acertijos lógicos, depuración, o cualquier tarea donde mostrar el proceso de razonamiento mejora la precisión y la confianza.

### Prompting Basado en Roles

Define una persona o rol para la IA antes de hacer tu pregunta. Esto provee contexto que da forma al tono, profundidad, y enfoque de la respuesta. Un "arquitecto de software" da consejos diferentes a un "desarrollador junior" o un "auditor de seguridad".

<img src="../../../translated_images/es/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting Basado en Roles" width="800"/>

*Establecer contexto y persona — la misma pregunta recibe una respuesta distinta según el rol asignado*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Cuándo usar:** Revisiones de código, tutorías, análisis de dominio específico, o cuando necesitas respuestas adaptadas a un nivel de experiencia o perspectiva particular.

### Plantillas de Prompts

Crea prompts reutilizables con marcadores de posición variables. En vez de escribir un prompt nuevo cada vez, define una plantilla una vez y rellena valores diferentes. La clase `PromptTemplate` de LangChain4j facilita esto con la sintaxis `{{variable}}`.

<img src="../../../translated_images/es/prompt-templates.14bfc37d45f1a933.webp" alt="Plantillas de Prompts" width="800"/>

*Prompts reutilizables con marcadores de posición variables — una plantilla, muchos usos*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Cuándo usar:** Consultas repetidas con diferentes entradas, procesamiento por lotes, construcción de flujos de trabajo AI reutilizables, o cualquier escenario donde la estructura del prompt permanece igual pero cambian los datos.

---

Estos cinco fundamentos te dan un conjunto sólido para la mayoría de tareas de prompting. El resto de este módulo se basa en ellos con **ocho patrones avanzados** que aprovechan el control de razonamiento, autoevaluación, y capacidades de salida estructurada de GPT-5.2.

## Patrones Avanzados

Con los fundamentos cubiertos, pasemos a los ocho patrones avanzados que hacen único este módulo. No todos los problemas necesitan el mismo enfoque. Algunas preguntas requieren respuestas rápidas, otras un pensamiento profundo. Algunas necesitan razonamiento visible, otras solo resultados. Cada patrón a continuación está optimizado para un escenario diferente — y el control de razonamiento de GPT-5.2 hace que las diferencias sean aún más notables.

<img src="../../../translated_images/es/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Ocho patrones de prompting" width="800"/>

*Resumen de los ocho patrones de ingeniería de prompt y sus casos de uso*

<img src="../../../translated_images/es/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Control de razonamiento con GPT-5.2" width="800"/>

*El control de razonamiento de GPT-5.2 te permite especificar cuánto debe pensar el modelo — desde respuestas rápidas y directas hasta exploración profunda*

<img src="../../../translated_images/es/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparación de esfuerzo de razonamiento" width="800"/>

*Disposición baja (rápido, directo) vs disposición alta (a fondo, exploratorio) en los enfoques de razonamiento*

**Disposición Baja (Rápido y Focalizado)** - Para preguntas simples donde quieres respuestas rápidas y directas. El modelo hace un razonamiento mínimo — máximo 2 pasos. Úsalo para cálculos, búsquedas, o preguntas directas.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explora con GitHub Copilot:** Abre [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) y pregunta:
> - "¿Cuál es la diferencia entre los patrones de prompting de baja y alta disposición?"
> - "¿Cómo ayudan las etiquetas XML en los prompts a estructurar la respuesta de la IA?"
> - "¿Cuándo debería usar patrones de autorreflexión versus instrucción directa?"

**Disposición Alta (Profundo y Exhaustivo)** - Para problemas complejos donde quieres análisis completo. El modelo explora en profundidad y muestra razonamientos detallados. Úsalo para diseño de sistemas, decisiones de arquitectura o investigación compleja.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ejecución de Tareas (Progreso Paso a Paso)** - Para flujos de trabajo con múltiples pasos. El modelo provee un plan inicial, narra cada paso mientras trabaja, luego da un resumen. Úsalo para migraciones, implementaciones o cualquier proceso multi-paso.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

El prompting Cadena de Pensamiento pide explícitamente al modelo mostrar su proceso de razonamiento, mejorando la precisión para tareas complejas. La descomposición paso a paso ayuda a humanos y a la IA a entender la lógica.

> **🤖 Prueba con Chat de [GitHub Copilot](https://github.com/features/copilot):** Pregunta sobre este patrón:
> - "¿Cómo adaptaría el patrón de ejecución de tareas para operaciones de larga duración?"
> - "¿Cuáles son las mejores prácticas para estructurar preámbulos de herramientas en aplicaciones productivas?"
> - "¿Cómo puedo captar y mostrar actualizaciones de progreso intermedias en una interfaz de usuario?"

<img src="../../../translated_images/es/task-execution-pattern.9da3967750ab5c1e.webp" alt="Patrón de Ejecución de Tareas" width="800"/>

*Planear → Ejecutar → Resumir flujo para tareas multi-paso*

**Código Auto-Reflexivo** - Para generar código de calidad productiva. El modelo genera código siguiendo estándares de producción con manejo adecuado de errores. Úsalo cuando construyas nuevas funcionalidades o servicios.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/es/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexión" width="800"/>

*Bucle de mejora iterativa - generar, evaluar, identificar problemas, mejorar, repetir*

**Análisis Estructurado** - Para evaluaciones consistentes. El modelo revisa código usando un marco fijo (corrección, prácticas, rendimiento, seguridad, mantenibilidad). Úsalo para revisiones de código o evaluaciones de calidad.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prueba con Chat de [GitHub Copilot](https://github.com/features/copilot):** Pregunta sobre análisis estructurado:
> - "¿Cómo puedo personalizar el marco de análisis para diferentes tipos de revisiones de código?"
> - "¿Cuál es la mejor manera de analizar y actuar sobre salida estructurada programáticamente?"
> - "¿Cómo aseguro niveles consistentes de severidad en diferentes sesiones de revisión?"

<img src="../../../translated_images/es/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Patrón de Análisis Estructurado" width="800"/>

*Marco para revisiones de código consistentes con niveles de severidad*

**Chat Multiturno** - Para conversaciones que necesitan contexto. El modelo recuerda mensajes previos y construye sobre ellos. Úsalo para sesiones de ayuda interactivas o preguntas y respuestas complejas.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/es/context-memory.dff30ad9fa78832a.webp" alt="Memoria de Contexto" width="800"/>

*Cómo el contexto de la conversación se acumula sobre múltiples turnos hasta alcanzar el límite de tokens*

**Razonamiento Paso a Paso** - Para problemas que requieren lógica visible. El modelo muestra razonamiento explícito para cada paso. Úsalo para problemas matemáticos, acertijos lógicos, o cuando necesites entender el proceso de pensamiento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/es/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Patrón Paso a Paso" width="800"/>

*Descomponer problemas en pasos lógicos explícitos*

**Salida Restringida** - Para respuestas con requisitos específicos de formato. El modelo sigue estrictamente reglas de formato y longitud. Úsalo para resúmenes o cuando necesites una estructura de salida precisa.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/es/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Patrón de Salida Restringida" width="800"/>

*Aplicar requisitos específicos de formato, longitud y estructura*

## Uso de Recursos Azure Existentes

**Verificar despliegue:**

Asegúrate que el archivo `.env` exista en el directorio raíz con las credenciales de Azure (creado durante el Módulo 01):
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` del Módulo 01, este módulo ya está corriendo en el puerto 8083. Puedes saltarte los comandos de inicio abajo e ir directamente a http://localhost:8083.

**Opción 1: Usar Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que provee una interfaz visual para gestionar todas las aplicaciones Spring Boot. Puedes encontrarla en la Barra de Actividades a la izquierda de VS Code (busca el ícono de Spring Boot).
Desde el Panel de Spring Boot, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver los registros de la aplicación en tiempo real
- Monitorizar el estado de la aplicación

Simplemente haz clic en el botón de reproducir junto a "prompt-engineering" para iniciar este módulo, o inicia todos los módulos a la vez.

<img src="../../../translated_images/es/dashboard.da2c2130c904aaf0.webp" alt="Panel de Spring Boot" width="400"/>

**Opción 2: Usando scripts de shell**

Iniciar todas las aplicaciones web (módulos 01-04):

**Bash:**
```bash
cd ..  # Desde el directorio raíz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Desde el directorio raíz
.\start-all.ps1
```

O iniciar solo este módulo:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Ambos scripts cargan automáticamente las variables de entorno del archivo raíz `.env` y construirán los JARs si no existen.

> **Nota:** Si prefieres construir todos los módulos manualmente antes de iniciar:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Abre http://localhost:8083 en tu navegador.

**Para detener:**

**Bash:**
```bash
./stop.sh  # Solo este módulo
# O
cd .. && ./stop-all.sh  # Todos los módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Solo este módulo
# O
cd ..; .\stop-all.ps1  # Todos los módulos
```

## Capturas de Pantalla de la Aplicación

<img src="../../../translated_images/es/dashboard-home.5444dbda4bc1f79d.webp" alt="Inicio del Panel" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*El panel principal que muestra los 8 patrones de ingeniería de prompts con sus características y casos de uso*

## Explorando los Patrones

La interfaz web te permite experimentar con diferentes estrategias de prompt. Cada patrón resuelve problemas distintos - pruébalos para ver cuándo brilla cada enfoque.

### Bajo vs Alto Entusiasmo

Haz una pregunta simple como "¿Cuál es el 15% de 200?" usando Bajo Entusiasmo. Obtendrás una respuesta instantánea y directa. Ahora haz algo complejo como "Diseña una estrategia de caché para una API con alto tráfico" usando Alto Entusiasmo. Observa cómo el modelo se ralentiza y proporciona razonamientos detallados. Mismo modelo, misma estructura de pregunta, pero el prompt le indica cuánto debe pensar.

<img src="../../../translated_images/es/low-eagerness-demo.898894591fb23aa0.webp" alt="Demostración de Bajo Entusiasmo" width="800"/>

*Cálculo rápido con razonamiento mínimo*

<img src="../../../translated_images/es/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demostración de Alto Entusiasmo" width="800"/>

*Estrategia de caché completa (2.8MB)*

### Ejecución de Tareas (Preámbulos de Herramientas)

Los flujos de trabajo de pasos múltiples se benefician de planificación anticipada y narración del progreso. El modelo detalla lo que hará, narra cada paso, luego resume los resultados.

<img src="../../../translated_images/es/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demostración de Ejecución de Tareas" width="800"/>

*Creación de un endpoint REST con narración paso a paso (3.9MB)*

### Código Auto-Reflexivo

Prueba "Crear un servicio de validación de correo electrónico". En lugar de solo generar código y detenerse, el modelo genera, evalúa criterios de calidad, identifica debilidades y mejora. Verás que itera hasta que el código cumple con los estándares de producción.

<img src="../../../translated_images/es/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demostración de Código Auto-Reflexivo" width="800"/>

*Servicio completo de validación de correo electrónico (5.2MB)*

### Análisis Estructurado

Las revisiones de código necesitan marcos de evaluación consistentes. El modelo analiza código usando categorías fijas (corrección, prácticas, rendimiento, seguridad) con niveles de severidad.

<img src="../../../translated_images/es/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demostración de Análisis Estructurado" width="800"/>

*Revisión de código basada en un marco*

### Chat Multi-Turno

Pregúntale "¿Qué es Spring Boot?" y luego sigue inmediatamente con "Muéstrame un ejemplo". El modelo recuerda tu primera pregunta y te da un ejemplo específico de Spring Boot. Sin memoria, esa segunda pregunta sería demasiado vaga.

<img src="../../../translated_images/es/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demostración de Chat Multi-Turno" width="800"/>

*Preservación del contexto a través de preguntas*

### Razonamiento Paso a Paso

Elige un problema de matemáticas y pruébalo con Razonamiento Paso a Paso y Bajo Entusiasmo. Bajo entusiasmo solo te da la respuesta - rápido pero opaco. Paso a paso te muestra cada cálculo y decisión.

<img src="../../../translated_images/es/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demostración de Razonamiento Paso a Paso" width="800"/>

*Problema matemático con pasos explícitos*

### Salida Restringida

Cuando necesites formatos específicos o número de palabras, este patrón hace cumplir una adherencia estricta. Prueba generar un resumen con exactamente 100 palabras en formato de viñetas.

<img src="../../../translated_images/es/constrained-output-demo.567cc45b75da1633.webp" alt="Demostración de Salida Restringida" width="800"/>

*Resumen de aprendizaje automático con control de formato*

## Lo Que Realmente Estás Aprendiendo

**El Esfuerzo de Razonamiento Cambia Todo**

GPT-5.2 te permite controlar el esfuerzo computacional a través de tus prompts. Bajo esfuerzo significa respuestas rápidas con exploración mínima. Alto esfuerzo significa que el modelo toma tiempo para pensar profundamente. Estás aprendiendo a ajustar el esfuerzo según la complejidad de la tarea - no desperdicies tiempo en preguntas simples, pero tampoco apresures decisiones complejas.

**La Estructura Guía el Comportamiento**

¿Notas las etiquetas XML en los prompts? No son decorativas. Los modelos siguen instrucciones estructuradas más confiablemente que texto libre. Cuando necesitas procesos de varios pasos o lógica compleja, la estructura ayuda al modelo a seguir el punto donde está y lo que viene después.

<img src="../../../translated_images/es/prompt-structure.a77763d63f4e2f89.webp" alt="Estructura del Prompt" width="800"/>

*Anatomía de un prompt bien estructurado con secciones claras y organización al estilo XML*

**Calidad Mediante Auto-Evaluación**

Los patrones auto-reflexivos funcionan haciendo explícitos los criterios de calidad. En lugar de esperar que el modelo "lo haga bien", le dices exactamente qué significa "bien": lógica correcta, manejo de errores, rendimiento, seguridad. Así el modelo puede evaluar su propia salida y mejorar. Esto convierte la generación de código de una lotería en un proceso.

**El Contexto Es Finito**

Las conversaciones multi-turno funcionan incluyendo el historial de mensajes en cada solicitud. Pero hay un límite - cada modelo tiene un máximo de tokens. A medida que las conversaciones crecen, necesitarás estrategias para mantener contexto relevante sin llegar a ese límite. Este módulo muestra cómo funciona la memoria; más adelante aprenderás cuándo resumir, cuándo olvidar y cuándo recuperar.

## Próximos Pasos

**Siguiente Módulo:** [03-rag - RAG (Generación Aumentada por Recuperación)](../03-rag/README.md)

---

**Navegación:** [← Anterior: Módulo 01 - Introducción](../01-introduction/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional humana. No nos hacemos responsables de ningún malentendido o interpretación errónea que pueda surgir del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->