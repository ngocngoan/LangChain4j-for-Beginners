# Módulo 01: Comenzando con LangChain4j

## Tabla de Contenidos

- [Recorrido en Video](../../../01-introduction)
- [Lo Que Aprenderás](../../../01-introduction)
- [Prerrequisitos](../../../01-introduction)
- [Entendiendo el Problema Central](../../../01-introduction)
- [Entendiendo los Tokens](../../../01-introduction)
- [Cómo Funciona la Memoria](../../../01-introduction)
- [Cómo Esto Usa LangChain4j](../../../01-introduction)
- [Implementar Infraestructura Azure OpenAI](../../../01-introduction)
- [Ejecutar la Aplicación Localmente](../../../01-introduction)
- [Usando la Aplicación](../../../01-introduction)
  - [Chat Sin Estado (Panel Izquierdo)](../../../01-introduction)
  - [Chat con Estado (Panel Derecho)](../../../01-introduction)
- [Próximos Pasos](../../../01-introduction)

## Recorrido en Video

Mira esta sesión en vivo que explica cómo comenzar con este módulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Comenzando con LangChain4j - Sesión en Vivo" width="800"/></a>

## Lo Que Aprenderás

En el inicio rápido, usaste Modelos de GitHub para enviar indicaciones, llamar herramientas, construir una tubería RAG y probar límites de seguridad. Esas demostraciones mostraron lo que es posible — ahora cambiamos a Azure OpenAI y GPT-5.2 para comenzar a construir aplicaciones al estilo producción. Este módulo se enfoca en IA conversacional que recuerda contexto y mantiene estado — los conceptos que esos demos de inicio rápido usaron detrás de escena pero no explicaron.

Usaremos GPT-5.2 de Azure OpenAI a lo largo de esta guía porque sus capacidades avanzadas de razonamiento hacen que el comportamiento de diferentes patrones sea más evidente. Cuando agregas memoria, verás claramente la diferencia. Esto facilita entender qué aporta cada componente a tu aplicación.

Construirás una aplicación que demuestra ambos patrones:

**Chat Sin Estado** - Cada solicitud es independiente. El modelo no recuerda mensajes previos. Este es el patrón que usaste en el inicio rápido.

**Conversación con Estado** - Cada solicitud incluye el historial de la conversación. El modelo mantiene el contexto a través de múltiples interacciones. Esto es lo que requieren las aplicaciones en producción.

## Prerrequisitos

- Suscripción de Azure con acceso a Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI y Azure Developer CLI (azd) están preinstalados en el devcontainer proporcionado.

> **Nota:** Este módulo usa GPT-5.2 en Azure OpenAI. La implementación se configura automáticamente vía `azd up` - no modifiques el nombre del modelo en el código.

## Entendiendo el Problema Central

Los modelos de lenguaje no mantienen estado. Cada llamada a la API es independiente. Si envías "Me llamo Juan" y luego preguntas "¿Cómo me llamo?", el modelo no sabe que acabas de presentarte. Trata cada solicitud como si fuera la primera conversación que tienes.

Esto está bien para preguntas y respuestas simples pero no sirve para aplicaciones reales. Los bots de atención al cliente necesitan recordar lo que les dijiste. Los asistentes personales necesitan contexto. Cualquier conversación de múltiples turnos requiere memoria.

El siguiente diagrama contrasta los dos enfoques — a la izquierda, una llamada sin estado que olvida tu nombre; a la derecha, una llamada con estado respaldada por ChatMemory que lo recuerda.

<img src="../../../translated_images/es/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversaciones sin estado vs con estado" width="800"/>

*La diferencia entre conversaciones sin estado (llamadas independientes) y con estado (con consciencia de contexto)*

## Entendiendo los Tokens

Antes de adentrarnos en las conversaciones, es importante entender los tokens - las unidades básicas de texto que procesan los modelos de lenguaje:

<img src="../../../translated_images/es/token-explanation.c39760d8ec650181.webp" alt="Explicación de Tokens" width="800"/>

*Ejemplo de cómo el texto se divide en tokens - "I love AI!" se convierte en 4 unidades separadas para procesamiento*

Los tokens son cómo los modelos de IA miden y procesan texto. Palabras, puntuación e incluso espacios pueden ser tokens. Tu modelo tiene un límite de cuántos tokens puede procesar a la vez (400,000 para GPT-5.2, con hasta 272,000 tokens de entrada y 128,000 de salida). Entender los tokens te ayuda a manejar la longitud de la conversación y los costos.

## Cómo Funciona la Memoria

La memoria de chat resuelve el problema de no mantener estado al conservar el historial de la conversación. Antes de enviar tu solicitud al modelo, el framework antepone mensajes previos relevantes. Cuando preguntas "¿Cómo me llamo?", el sistema en realidad envía todo el historial de la conversación, permitiendo al modelo ver que previamente dijiste "Me llamo Juan."

LangChain4j provee implementaciones de memoria que manejan esto automáticamente. Tú eliges cuántos mensajes conservar y el framework administra la ventana de contexto. El siguiente diagrama muestra cómo MessageWindowChatMemory mantiene una ventana deslizante de mensajes recientes.

<img src="../../../translated_images/es/memory-window.bbe67f597eadabb3.webp" alt="Concepto de Ventana de Memoria" width="800"/>

*MessageWindowChatMemory mantiene una ventana deslizante de mensajes recientes, descartando automáticamente los más antiguos*

## Cómo Esto Usa LangChain4j

Este módulo extiende el inicio rápido integrando Spring Boot y añadiendo memoria a la conversación. Así es como encajan las piezas:

**Dependencias** - Añade dos librerías de LangChain4j:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Modelo de Chat** - Configura Azure OpenAI como un bean de Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

El constructor lee credenciales de variables de entorno establecidas por `azd up`. Configurar `baseUrl` a tu endpoint de Azure hace que el cliente OpenAI funcione con Azure OpenAI.

**Memoria de Conversación** - Rastrea el historial del chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea la memoria con `withMaxMessages(10)` para conservar los últimos 10 mensajes. Añade mensajes de usuario e IA con los envoltorios tipados: `UserMessage.from(text)` y `AiMessage.from(text)`. Recupera el historial con `memory.messages()` y envíalo al modelo. El servicio almacena instancias de memoria separadas por ID de conversación, permitiendo que múltiples usuarios chateen simultáneamente.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) y pregunta:
> - "¿Cómo decide MessageWindowChatMemory qué mensajes eliminar cuando la ventana está llena?"
> - "¿Puedo implementar almacenamiento de memoria personalizado usando una base de datos en lugar de memoria en RAM?"
> - "¿Cómo agregaría una función de resumen para comprimir históricamente conversaciones antiguas?"

El endpoint de chat sin estado omite la memoria por completo - solo `chatModel.chat(prompt)` como en el inicio rápido. El endpoint con estado añade mensajes a la memoria, recupera el historial e incluye ese contexto con cada solicitud. Misma configuración del modelo, diferentes patrones.

## Implementar Infraestructura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Seleccione la suscripción y la ubicación (se recomienda eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Seleccione la suscripción y la ubicación (se recomienda eastus2)
```

> **Nota:** Si encuentras un error de tiempo de espera (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), simplemente ejecuta `azd up` nuevamente. Los recursos de Azure aún podrían estar aprovisionándose en segundo plano, y reintentar permite que la implementación se complete una vez que los recursos alcancen un estado final.

Esto hará:
1. Desplegar el recurso Azure OpenAI con modelos GPT-5.2 y text-embedding-3-small
2. Generar automáticamente el archivo `.env` en la raíz del proyecto con credenciales
3. Configurar todas las variables de entorno requeridas

**¿Problemas con la implementación?** Consulta el [README de Infraestructura](infra/README.md) para resolver problemas detallados incluyendo conflictos de nombres de subdominios, pasos de implementación manual en Azure Portal y guías para configuración de modelos.

**Verifica que la implementación haya sido exitosa:**

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** El comando `azd up` genera automáticamente el archivo `.env`. Si necesitas actualizarlo después, puedes editar el archivo manualmente o regenerarlo ejecutando:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Ejecutar la Aplicación Localmente

**Verificar implementación:**

Asegúrate que el archivo `.env` exista en el directorio raíz con las credenciales de Azure. Ejecuta esto desde el directorio del módulo (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar las aplicaciones:**

**Opción 1: Usando Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El contenedor de desarrollo incluye la extensión Spring Boot Dashboard, que ofrece una interfaz visual para gestionar todas las aplicaciones Spring Boot. Puedes encontrarla en la Barra de Actividades a la izquierda de VS Code (busca el ícono de Spring Boot).

Desde el Spring Boot Dashboard, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/detener aplicaciones con un solo clic
- Ver los registros de la aplicación en tiempo real
- Monitorear el estado de la aplicación

Simplemente haz clic en el botón de reproducir junto a "introduction" para iniciar este módulo o inicia todos los módulos a la vez.

<img src="../../../translated_images/es/dashboard.69c7479aef09ff6b.webp" alt="Panel de Spring Boot" width="400"/>

*El Spring Boot Dashboard en VS Code — inicia, detén y monitorea todos los módulos desde un solo lugar*

**Opción 2: Usar scripts de shell**

Inicia todas las aplicaciones web (módulos 01-04):

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

O inicia solo este módulo:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Ambos scripts cargan automáticamente variables de entorno del archivo `.env` raíz y construirán los JARs si no existen.

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

Abre http://localhost:8080 en tu navegador.

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

## Usando la Aplicación

La aplicación provee una interfaz web con dos implementaciones de chat lado a lado.

<img src="../../../translated_images/es/home-screen.121a03206ab910c0.webp" alt="Pantalla Principal de la Aplicación" width="800"/>

*Panel mostrando opciones para Chat Simple (sin estado) y Chat Conversacional (con estado)*

### Chat Sin Estado (Panel Izquierdo)

Prueba primero aquí. Di "Me llamo Juan" y luego inmediatamente pregunta "¿Cómo me llamo?" El modelo no recordará porque cada mensaje es independiente. Esto demuestra el problema central con la integración básica de modelos de lenguaje - no hay contexto de conversación.

<img src="../../../translated_images/es/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demostración de Chat Sin Estado" width="800"/>

*La IA no recuerda tu nombre del mensaje anterior*

### Chat con Estado (Panel Derecho)

Ahora prueba la misma secuencia aquí. Di "Me llamo Juan" y luego "¿Cómo me llamo?" Esta vez lo recuerda. La diferencia es MessageWindowChatMemory - mantiene el historial de la conversación e incluye ese contexto con cada solicitud. Así funciona la IA conversacional en producción.

<img src="../../../translated_images/es/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demostración de Chat con Estado" width="800"/>

*La IA recuerda tu nombre desde el inicio de la conversación*

Ambos paneles usan el mismo modelo GPT-5.2. La única diferencia es la memoria. Esto deja claro qué aporta la memoria a tu aplicación y por qué es esencial para casos reales.

## Próximos Pasos

**Siguiente Módulo:** [02-prompt-engineering - Ingeniería de Prompts con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegación:** [← Anterior: Módulo 00 - Inicio Rápido](../00-quick-start/README.md) | [Volver al Inicio](../README.md) | [Siguiente: Módulo 02 - Ingeniería de Prompts →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por lograr precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por un humano. No nos hacemos responsables de malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->