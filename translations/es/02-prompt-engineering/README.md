# Módulo 02: Ingeniería de Prompts con GPT-5.2

## Tabla de Contenidos

- [Lo que Aprenderás](../../../02-prompt-engineering)
- [Prerrequisitos](../../../02-prompt-engineering)
- [Entendiendo la Ingeniería de Prompts](../../../02-prompt-engineering)
- [Fundamentos de la Ingeniería de Prompts](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Cadena de Pensamiento](../../../02-prompt-engineering)
  - [Prompting Basado en Roles](../../../02-prompt-engineering)
  - [Plantillas de Prompts](../../../02-prompt-engineering)
- [Patrones Avanzados](../../../02-prompt-engineering)
- [Uso de Recursos Existentes en Azure](../../../02-prompt-engineering)
- [Capturas de Pantalla de la Aplicación](../../../02-prompt-engineering)
- [Explorando los Patrones](../../../02-prompt-engineering)
  - [Baja vs Alta Disposición](../../../02-prompt-engineering)
  - [Ejecución de Tareas (Preámbulos de Herramientas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análisis Estructurado](../../../02-prompt-engineering)
  - [Chat Multiturno](../../../02-prompt-engineering)
  - [Razonamiento Paso a Paso](../../../02-prompt-engineering)
  - [Salida Restringida](../../../02-prompt-engineering)
- [Lo Que Realmente Estás Aprendiendo](../../../02-prompt-engineering)
- [Próximos Pasos](../../../02-prompt-engineering)

## Lo que Aprenderás

<img src="../../../translated_images/es/what-youll-learn.c68269ac048503b2.webp" alt="Lo que Aprenderás" width="800"/>

En el módulo anterior, viste cómo la memoria habilita la IA conversacional y usaste Modelos de GitHub para interacciones básicas. Ahora nos enfocaremos en cómo hacer preguntas — los prompts en sí — usando GPT-5.2 de Azure OpenAI. La forma en que estructuras tus prompts afecta drásticamente la calidad de las respuestas que obtienes. Comenzamos con una revisión de las técnicas fundamentales de prompting, y luego pasamos a ocho patrones avanzados que aprovechan al máximo las capacidades de GPT-5.2.

Usaremos GPT-5.2 porque introduce control de razonamiento: puedes indicarle al modelo cuánto pensamiento debe hacer antes de responder. Esto hace que las diferentes estrategias de prompting sean más evidentes y te ayuda a entender cuándo usar cada enfoque. También nos beneficiaremos de los menores límites de tasa de Azure para GPT-5.2 en comparación con los Modelos de GitHub.

## Prerrequisitos

- Módulo 01 completado (recursos Azure OpenAI desplegados)
- Archivo `.env` en el directorio raíz con credenciales de Azure (creado por `azd up` en el Módulo 01)

> **Nota:** Si no has completado el Módulo 01, sigue primero las instrucciones de despliegue allí.

## Entendiendo la Ingeniería de Prompts

<img src="../../../translated_images/es/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="¿Qué es la Ingeniería de Prompts?" width="800"/>

La ingeniería de prompts trata de diseñar texto de entrada que consistentemente te da los resultados que necesitas. No se trata solo de hacer preguntas — se trata de estructurar las solicitudes para que el modelo entienda exactamente qué quieres y cómo entregarlo.

Piénsalo como dar instrucciones a un colega. "Arregla el bug" es vago. "Arregla la excepción de puntero nulo en UserService.java línea 45 agregando una verificación de nulos" es específico. Los modelos de lenguaje funcionan igual — la especificidad y la estructura importan.

<img src="../../../translated_images/es/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Cómo encaja LangChain4j" width="800"/>

LangChain4j provee la infraestructura — conexiones de modelo, memoria y tipos de mensaje — mientras que los patrones de prompts son simplemente texto cuidadosamente estructurado que envías a través de esa infraestructura. Los bloques clave son `SystemMessage` (que establece el comportamiento y rol de la IA) y `UserMessage` (que lleva tu solicitud real).

## Fundamentos de la Ingeniería de Prompts

<img src="../../../translated_images/es/five-patterns-overview.160f35045ffd2a94.webp" alt="Resumen de Cinco Patrones de Ingeniería de Prompts" width="800"/>

Antes de sumergirnos en los patrones avanzados de este módulo, revisemos cinco técnicas fundamentales de prompting. Estos son los bloques de construcción que todo ingeniero de prompts debe conocer. Si ya trabajaste el [módulo de Inicio Rápido](../00-quick-start/README.md#2-prompt-patterns), los viste en acción — aquí está el marco conceptual detrás de ellos.

### Prompting Zero-Shot

El enfoque más simple: darle al modelo una instrucción directa sin ejemplos. El modelo se basa completamente en su entrenamiento para entender y ejecutar la tarea. Esto funciona bien para solicitudes directas donde el comportamiento esperado es obvio.

<img src="../../../translated_images/es/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instrucción directa sin ejemplos — el modelo infiere la tarea solo de la instrucción*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respuesta: "Positivo"
```

**Cuándo usar:** Clasificaciones simples, preguntas directas, traducciones, o cualquier tarea que el modelo pueda manejar sin guía adicional.

### Prompting Few-Shot

Proporciona ejemplos que demuestran el patrón que quieres que el modelo siga. El modelo aprende el formato esperado entrada-salida a partir de tus ejemplos y lo aplica a nuevas entradas. Esto mejora drásticamente la consistencia para tareas donde el formato o comportamiento deseado no es obvio.

<img src="../../../translated_images/es/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Aprender de ejemplos — el modelo identifica el patrón y lo aplica a nuevas entradas*

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

**Cuándo usar:** Clasificaciones personalizadas, formatos consistentes, tareas específicas de dominio, o cuando los resultados zero-shot son inconsistentes.

### Cadena de Pensamiento

Pide al modelo que muestre su razonamiento paso a paso. En lugar de saltar directamente a una respuesta, el modelo descompone el problema y trabaja cada parte explícitamente. Esto mejora la precisión en matemáticas, lógica y razonamientos de múltiples pasos.

<img src="../../../translated_images/es/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting Cadena de Pensamiento" width="800"/>

*Razonamiento paso a paso — descomponiendo problemas complejos en pasos lógicos explícitos*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// El modelo muestra: 15 - 8 = 7, luego 7 + 12 = 19 manzanas
```

**Cuándo usar:** Problemas matemáticos, acertijos lógicos, depuración, o cualquier tarea donde mostrar el proceso de razonamiento mejora la precisión y confianza.

### Prompting Basado en Roles

Establece una persona o rol para la IA antes de hacer tu pregunta. Esto proporciona contexto que moldea el tono, la profundidad y el enfoque de la respuesta. Un "arquitecto de software" da consejos diferentes que un "desarrollador junior" o un "auditor de seguridad".

<img src="../../../translated_images/es/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting Basado en Roles" width="800"/>

*Establecimiento de contexto y persona — la misma pregunta recibe diferentes respuestas según el rol asignado*

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

**Cuándo usar:** Revisiones de código, tutoría, análisis específicos de dominio, o cuando necesitas respuestas adaptadas a un nivel de experiencia o perspectiva particular.

### Plantillas de Prompts

Crea prompts reutilizables con marcadores de posición variables. En lugar de escribir un prompt nuevo cada vez, define una plantilla una vez y rellena diferentes valores. La clase `PromptTemplate` de LangChain4j facilita esto con la sintaxis `{{variable}}`.

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

**Cuándo usar:** Consultas repetidas con diferentes entradas, procesamiento por lotes, construcción de flujos de trabajo de IA reutilizables, o cualquier escenario donde la estructura del prompt se mantiene pero los datos cambian.

---

Estos cinco fundamentos te dan una caja de herramientas sólida para la mayoría de las tareas de prompting. El resto de este módulo se basa en ellos con **ocho patrones avanzados** que aprovechan el control de razonamiento, autoevaluación, y capacidades de salida estructurada de GPT-5.2.

## Patrones Avanzados

Con lo fundamental cubierto, pasemos a los ocho patrones avanzados que hacen único este módulo. No todos los problemas necesitan el mismo enfoque. Algunas preguntas requieren respuestas rápidas, otras un pensamiento profundo. Algunas necesitan razonamiento visible, otras solo resultados. Cada patrón abajo está optimizado para un escenario diferente — y el control de razonamiento de GPT-5.2 hace que las diferencias sean aún más notables.

<img src="../../../translated_images/es/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Ocho Patrones de Prompting" width="800"/>

*Resumen de los ocho patrones de ingeniería de prompts y sus casos de uso*

<img src="../../../translated_images/es/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Control de Razonamiento con GPT-5.2" width="800"/>

*El control de razonamiento de GPT-5.2 te permite especificar cuánto pensamiento debe hacer el modelo — desde respuestas rápidas y directas hasta exploración profunda*

<img src="../../../translated_images/es/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparación de Esfuerzo de Razonamiento" width="800"/>

*Disposición baja (rápido, directo) vs disposición alta (exhaustivo, exploratorio) en enfoques de razonamiento*

**Disposición Baja (Rápido y Enfocado)** - Para preguntas simples donde quieres respuestas rápidas y directas. El modelo hace razonamiento mínimo — máximo 2 pasos. Úsalo para cálculos, consultas o preguntas directas.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explora con GitHub Copilot:** Abre [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) y pregunta:
> - "¿Cuál es la diferencia entre los patrones de prompting de baja y alta disposición?"
> - "¿Cómo ayudan las etiquetas XML en los prompts a estructurar la respuesta de la IA?"
> - "¿Cuándo debo usar patrones de auto-reflexión vs instrucción directa?"

**Disposición Alta (Profundo y Exhaustivo)** - Para problemas complejos donde quieres un análisis completo. El modelo explora a fondo y muestra razonamiento detallado. Úsalo para diseño de sistemas, decisiones arquitectónicas o investigación compleja.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Ejecución de Tareas (Progreso Paso a Paso)** - Para flujos de trabajo de múltiples pasos. El modelo provee un plan inicial, narra cada paso mientras trabaja, y después da un resumen. Úsalo para migraciones, implementaciones o cualquier proceso múltiple.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

El prompting de Cadena de Pensamiento pide explícitamente al modelo mostrar su proceso de razonamiento, mejorando la precisión en tareas complejas. La descomposición paso a paso ayuda tanto a humanos como a la IA a entender la lógica.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Pregunta sobre este patrón:
> - "¿Cómo adaptaría el patrón de ejecución de tareas para operaciones de larga duración?"
> - "¿Cuáles son las mejores prácticas para estructurar preámbulos de herramientas en aplicaciones productivas?"
> - "¿Cómo capturo y muestro actualizaciones de progreso intermedias en una interfaz de usuario?"

<img src="../../../translated_images/es/task-execution-pattern.9da3967750ab5c1e.webp" alt="Patrón de Ejecución de Tareas" width="800"/>

*Flujo Plan → Ejecutar → Resumir para tareas de múltiples pasos*

**Código Auto-Reflexivo** - Para generar código de calidad productiva. El modelo genera código, lo verifica contra criterios de calidad y lo mejora iterativamente. Úsalo cuando construyas nuevas funcionalidades o servicios.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/es/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexión" width="800"/>

*Bucle de mejora iterativa - generar, evaluar, identificar problemas, mejorar, repetir*

**Análisis Estructurado** - Para evaluaciones consistentes. El modelo revisa código usando un marco fijo (correctitud, prácticas, rendimiento, seguridad). Úsalo para revisiones de código o evaluaciones de calidad.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Pregunta sobre análisis estructurado:
> - "¿Cómo personalizo el marco de análisis para distintos tipos de revisiones de código?"
> - "¿Cuál es la mejor forma de parsear y actuar sobre salidas estructuradas programáticamente?"
> - "¿Cómo garantizo niveles consistentes de severidad en diferentes sesiones de revisión?"

<img src="../../../translated_images/es/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Patrón de Análisis Estructurado" width="800"/>

*Marco de cuatro categorías para revisiones de código consistentes con niveles de severidad*

**Chat Multiturno** - Para conversaciones que necesitan contexto. El modelo recuerda mensajes previos y construye sobre ellos. Úsalo para sesiones de ayuda interactivas o preguntas complejas.

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

*Cómo el contexto de la conversación se acumula durante múltiples turnos hasta alcanzar el límite de tokens*

**Razonamiento Paso a Paso** - Para problemas que requieren lógica visible. El modelo muestra razonamiento explícito para cada paso. Úsalo para problemas matemáticos, acertijos lógicos, o cuando necesitas entender el proceso de pensamiento.

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

**Salida Restringida** - Para respuestas con requisitos de formato específicos. El modelo sigue estrictamente reglas de formato y longitud. Úsalo para resúmenes o cuando necesites una estructura precisa de salida.

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

*Forzando requisitos específicos de formato, longitud y estructura*

## Uso de Recursos Existentes en Azure

**Verificar despliegue:**

Asegura que el archivo `.env` existe en el directorio raíz con credenciales de Azure (creado durante el Módulo 01):
```bash
cat ../.env  # Debería mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar la aplicación:**

> **Nota:** Si ya iniciaste todas las aplicaciones usando `./start-all.sh` del Módulo 01, este módulo ya está corriendo en el puerto 8083. Puedes omitir los comandos de inicio abajo y entrar directamente a http://localhost:8083.

**Opción 1: Usando Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que provee una interfaz visual para administrar todas las aplicaciones Spring Boot. Puedes encontrarla en la Barra de Actividades al lado izquierdo de VS Code (busca el ícono de Spring Boot).
Desde el Panel de Spring Boot, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver los registros de la aplicación en tiempo real
- Monitorear el estado de la aplicación

Simplemente haz clic en el botón de reproducción junto a "prompt-engineering" para iniciar este módulo, o inicia todos los módulos a la vez.

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

Ambos scripts cargan automáticamente las variables de entorno desde el archivo `.env` raíz y construirán los JARs si no existen.

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

## Capturas de pantalla de la aplicación

<img src="../../../translated_images/es/dashboard-home.5444dbda4bc1f79d.webp" alt="Inicio del Panel" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*El panel principal muestra los 8 patrones de ingeniería de prompts con sus características y casos de uso*

## Explorando los Patrones

La interfaz web te permite experimentar con diferentes estrategias de prompt. Cada patrón resuelve distintos problemas: pruébalos para ver cuándo brilla cada enfoque.

### Baja vs Alta Disposición

Haz una pregunta simple como "¿Cuál es el 15% de 200?" usando Baja Disposición. Obtendrás una respuesta instantánea y directa. Ahora haz algo complejo como "Diseña una estrategia de caché para una API con alto tráfico" usando Alta Disposición. Observa cómo el modelo se toma su tiempo y ofrece razonamientos detallados. Mismo modelo, misma estructura de pregunta, pero el prompt le indica cuánto pensar.

<img src="../../../translated_images/es/low-eagerness-demo.898894591fb23aa0.webp" alt="Demostración de Baja Disposición" width="800"/>

*Cálculo rápido con razonamiento mínimo*

<img src="../../../translated_images/es/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demostración de Alta Disposición" width="800"/>

*Estrategia de caché completa (2.8MB)*

### Ejecución de Tareas (Preámbulos de Herramientas)

Los flujos de trabajo de múltiples pasos se benefician de una planificación anticipada y narración de progreso. El modelo describe lo que hará, narra cada paso y luego resume los resultados.

<img src="../../../translated_images/es/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demostración de Ejecución de Tareas" width="800"/>

*Creando un endpoint REST con narración paso a paso (3.9MB)*

### Código Auto-Reflexivo

Prueba "Crear un servicio de validación de correo electrónico". En lugar de solo generar el código y detenerse, el modelo genera, evalúa según criterios de calidad, identifica debilidades y mejora. Verás cómo itera hasta que el código cumple con los estándares de producción.

<img src="../../../translated_images/es/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demostración de Código Auto-Reflexivo" width="800"/>

*Servicio completo de validación de correo electrónico (5.2MB)*

### Análisis Estructurado

Las revisiones de código requieren marcos de evaluación consistentes. El modelo analiza el código usando categorías fijas (corrección, prácticas, rendimiento, seguridad) con niveles de severidad.

<img src="../../../translated_images/es/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demostración de Análisis Estructurado" width="800"/>

*Revisión de código basada en un marco estructurado*

### Chat Multi-Turno

Pregunta "¿Qué es Spring Boot?" y luego inmediatamente sigue con "Muéstrame un ejemplo". El modelo recuerda tu primera pregunta y te da un ejemplo específico de Spring Boot. Sin memoria, esa segunda pregunta sería demasiado vaga.

<img src="../../../translated_images/es/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demostración de Chat Multi-Turno" width="800"/>

*Preservación del contexto entre preguntas*

### Razonamiento Paso a Paso

Elige un problema matemático y pruébalo tanto con Razonamiento Paso a Paso como con Baja Disposición. Baja disposición solo te da la respuesta rápido pero sin claridad. El razonamiento paso a paso te muestra cada cálculo y decisión.

<img src="../../../translated_images/es/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demostración de Razonamiento Paso a Paso" width="800"/>

*Problema matemático con pasos explícitos*

### Salida Restringida

Cuando necesitas formatos o conteos de palabras específicos, este patrón impone una adhesión estricta. Prueba generar un resumen con exactamente 100 palabras en formato de lista.

<img src="../../../translated_images/es/constrained-output-demo.567cc45b75da1633.webp" alt="Demostración de Salida Restringida" width="800"/>

*Resumen de aprendizaje automático con control de formato*

## Lo Que Realmente Estás Aprendiendo

**El Esfuerzo de Razonamiento Cambia Todo**

GPT-5.2 te permite controlar el esfuerzo computacional a través de tus prompts. Bajo esfuerzo significa respuestas rápidas con exploración mínima. Alto esfuerzo significa que el modelo se toma tiempo para pensar profundo. Estás aprendiendo a ajustar el esfuerzo a la complejidad de la tarea: no pierdas tiempo en preguntas simples, pero tampoco apresures decisiones complejas.

**La Estructura Guía el Comportamiento**

¿Notas las etiquetas XML en los prompts? No son decorativas. Los modelos siguen instrucciones estructuradas de manera más confiable que texto libre. Cuando necesitas procesos de múltiples pasos o lógica compleja, la estructura ayuda al modelo a saber dónde está y qué sigue.

<img src="../../../translated_images/es/prompt-structure.a77763d63f4e2f89.webp" alt="Estructura del Prompt" width="800"/>

*Anatomía de un prompt bien estructurado con secciones claras y organización al estilo XML*

**Calidad a Través de la Autoevaluación**

Los patrones auto-reflexivos funcionan haciendo explícitos los criterios de calidad. En lugar de esperar que el modelo “lo haga bien”, le dices exactamente qué significa “bien”: lógica correcta, manejo de errores, rendimiento, seguridad. Entonces el modelo puede evaluar su propia salida y mejorar. Esto convierte la generación de código de una lotería en un proceso.

**El Contexto Es Finito**

Las conversaciones multi-turno funcionan incluyendo el historial de mensajes en cada solicitud. Pero hay un límite: cada modelo tiene un máximo de tokens. A medida que crecen las conversaciones, necesitarás estrategias para mantener el contexto relevante sin superar ese límite. Este módulo te muestra cómo funciona la memoria; más adelante aprenderás cuándo resumir, cuándo olvidar y cuándo recuperar.

## Próximos Pasos

**Próximo módulo:** [03-rag - RAG (Generación Aumentada por Recuperación)](../03-rag/README.md)

---

**Navegación:** [← Anterior: Módulo 01 - Introducción](../01-introduction/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por un humano. No nos hacemos responsables de ningún malentendido o interpretación errónea que resulte del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->