# Módulo 01: Comenzando con LangChain4j

## Tabla de Contenidos

- [Video Guía](../../../01-introduction)
- [Lo Que Aprenderás](../../../01-introduction)
- [Prerequisitos](../../../01-introduction)
- [Entendiendo el Problema Principal](../../../01-introduction)
- [Entendiendo los Tokens](../../../01-introduction)
- [Cómo Funciona la Memoria](../../../01-introduction)
- [Cómo Esto Usa LangChain4j](../../../01-introduction)
- [Desplegar Infraestructura Azure OpenAI](../../../01-introduction)
- [Ejecutar la Aplicación Localmente](../../../01-introduction)
- [Usando la Aplicación](../../../01-introduction)
  - [Chat Sin Estado (Panel Izquierdo)](../../../01-introduction)
  - [Chat con Estado (Panel Derecho)](../../../01-introduction)
- [Próximos Pasos](../../../01-introduction)

## Video Guía

Mira esta sesión en vivo que explica cómo comenzar con este módulo: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Lo Que Aprenderás

Si completaste el inicio rápido, viste cómo enviar prompts y obtener respuestas. Esa es la base, pero las aplicaciones reales necesitan más. Este módulo te enseña a construir una IA conversacional que recuerda el contexto y mantiene el estado - la diferencia entre una demo puntual y una aplicación lista para producción.

Usaremos GPT-5.2 de Azure OpenAI a lo largo de esta guía porque sus capacidades avanzadas de razonamiento hacen que el comportamiento de diferentes patrones sea más evidente. Cuando añades memoria, verás claramente la diferencia. Esto facilita entender lo que cada componente aporta a tu aplicación.

Construirás una aplicación que demuestra ambos patrones:

**Chat Sin Estado** - Cada solicitud es independiente. El modelo no tiene memoria de mensajes anteriores. Este es el patrón que usaste en el inicio rápido.

**Conversación con Estado** - Cada solicitud incluye el historial de conversación. El modelo mantiene el contexto a través de múltiples turnos. Esto es lo que requieren las aplicaciones en producción.

## Prerequisitos

- Suscripción de Azure con acceso a Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI y Azure Developer CLI (azd) están preinstalados en el devcontainer proporcionado.

> **Nota:** Este módulo usa GPT-5.2 en Azure OpenAI. El despliegue se configura automáticamente vía `azd up` - no modifiques el nombre del modelo en el código.

## Entendiendo el Problema Principal

Los modelos de lenguaje son sin estado. Cada llamada a la API es independiente. Si envías "Mi nombre es Juan" y luego preguntas "¿Cuál es mi nombre?", el modelo no tiene idea que acabas de presentarte. Trata cada solicitud como si fuera la primera conversación que tienes.

Esto está bien para preguntas y respuestas simples pero es inútil para aplicaciones reales. Los bots de servicio al cliente necesitan recordar lo que les dijiste. Los asistentes personales necesitan contexto. Cualquier conversación de múltiples turnos requiere memoria.

<img src="../../../translated_images/es/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversaciones sin estado vs con estado" width="800"/>

*La diferencia entre conversaciones sin estado (llamadas independientes) y con estado (con conciencia de contexto)*

## Entendiendo los Tokens

Antes de sumergirte en las conversaciones, es importante entender los tokens - las unidades básicas de texto que los modelos de lenguaje procesan:

<img src="../../../translated_images/es/token-explanation.c39760d8ec650181.webp" alt="Explicación de Tokens" width="800"/>

*Ejemplo de cómo el texto se divide en tokens - "¡Me encanta la IA!" se convierte en 4 unidades separadas para procesar*

Los tokens son cómo los modelos AI miden y procesan texto. Las palabras, puntuación e incluso espacios pueden ser tokens. Tu modelo tiene un límite de cuántos tokens puede procesar a la vez (400,000 para GPT-5.2, con hasta 272,000 tokens de entrada y 128,000 tokens de salida). Entender los tokens te ayuda a administrar la longitud de las conversaciones y los costos.

## Cómo Funciona la Memoria

La memoria del chat resuelve el problema sin estado al mantener el historial de la conversación. Antes de enviar tu solicitud al modelo, el framework antepone mensajes anteriores relevantes. Cuando preguntas "¿Cuál es mi nombre?", el sistema realmente envía todo el historial de conversación, permitiendo que el modelo vea que previamente dijiste "Mi nombre es Juan."

LangChain4j provee implementaciones de memoria que manejan esto automáticamente. Tú eliges cuántos mensajes mantener y el framework administra la ventana de contexto.

<img src="../../../translated_images/es/memory-window.bbe67f597eadabb3.webp" alt="Concepto de Ventana de Memoria" width="800"/>

*MessageWindowChatMemory mantiene una ventana deslizante de mensajes recientes, eliminando automáticamente los más antiguos*

## Cómo Esto Usa LangChain4j

Este módulo extiende el inicio rápido integrando Spring Boot y agregando memoria a la conversación. Así es como encajan las piezas:

**Dependencias** - Agrega dos librerías LangChain4j:

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

**Modelo de Chat** - Configura Azure OpenAI como un bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

El builder lee las credenciales de variables de ambiente establecidas por `azd up`. Configurar `baseUrl` al endpoint de Azure hace que el cliente OpenAI funcione con Azure OpenAI.

**Memoria de Conversación** - Rastrear el historial de chat con MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crea memoria con `withMaxMessages(10)` para mantener los últimos 10 mensajes. Añade mensajes de usuario e IA con wrappers tipados: `UserMessage.from(text)` y `AiMessage.from(text)`. Recupera el historial con `memory.messages()` y envíalo al modelo. El servicio almacena instancias de memoria por ID de conversación, permitiendo que múltiples usuarios chateen simultáneamente.

> **🤖 Prueba con [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) y pregunta:
> - "¿Cómo decide MessageWindowChatMemory qué mensajes eliminar cuando la ventana está llena?"
> - "¿Puedo implementar almacenamiento de memoria personalizado usando una base de datos en lugar de memoria en RAM?"
> - "¿Cómo agregaría resumen para comprimir el historial antiguo de la conversación?"

El endpoint de chat sin estado omite la memoria totalmente - solo `chatModel.chat(prompt)` como en el inicio rápido. El endpoint con estado añade mensajes a memoria, recupera el historial e incluye ese contexto en cada solicitud. Mismo modelo configurado, diferentes patrones.

## Desplegar Infraestructura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Seleccione la suscripción y la ubicación (se recomienda eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Seleccione suscripción y ubicación (se recomienda eastus2)
```

> **Nota:** Si encuentras un error de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), simplemente ejecuta `azd up` otra vez. Los recursos de Azure pueden estar aún aprovisionándose en segundo plano, y reintentar permite que el despliegue se complete cuando los recursos alcancen un estado terminal.

Esto hará:
1. Desplegar recurso Azure OpenAI con modelos GPT-5.2 y text-embedding-3-small
2. Generar automáticamente archivo `.env` en la raíz del proyecto con credenciales
3. Configurar todas las variables de entorno requeridas

**¿Problemas con el despliegue?** Consulta el [README de Infraestructura](infra/README.md) para resolución detallada incluyendo conflictos de nombre de subdominio, pasos manuales en Azure Portal y guía de configuración de modelos.

**Verifica que el despliegue fue exitoso:**

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** El comando `azd up` genera automáticamente el archivo `.env`. Si necesitas actualizarlo luego, puedes editar el `.env` manualmente o regenerarlo ejecutando:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Ejecutar la Aplicación Localmente

**Verifica el despliegue:**

Asegúrate que el archivo `.env` exista en el directorio raíz con credenciales de Azure:

**Bash:**
```bash
cat ../.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Debe mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicia las aplicaciones:**

**Opción 1: Usar Spring Boot Dashboard (Recomendado para usuarios de VS Code)**

El devcontainer incluye la extensión Spring Boot Dashboard, que provee una interfaz visual para gestionar todas las aplicaciones Spring Boot. La encontrarás en la Barra de Actividades al lado izquierdo de VS Code (busca el ícono de Spring Boot).

Desde Spring Boot Dashboard, puedes:
- Ver todas las aplicaciones Spring Boot disponibles en el espacio de trabajo
- Iniciar/parar aplicaciones con un clic
- Visualizar logs de la aplicación en tiempo real
- Monitorear el estado de la aplicación

Simplemente haz clic en el botón de play junto a "introduction" para iniciar este módulo, o inicia todos los módulos al mismo tiempo.

<img src="../../../translated_images/es/dashboard.69c7479aef09ff6b.webp" alt="Panel de Control de Spring Boot" width="400"/>

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

Ambos scripts cargan automáticamente las variables de entorno del archivo `.env` raíz y construirán los JARs si no existen.

> **Nota:** Si prefieres construir manualmente todos los módulos antes de iniciar:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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

<img src="../../../translated_images/es/home-screen.121a03206ab910c0.webp" alt="Pantalla de Inicio de la Aplicación" width="800"/>

*Panel mostrando ambas opciones Simple Chat (sin estado) y Conversational Chat (con estado)*

### Chat Sin Estado (Panel Izquierdo)

Prueba esto primero. Di "Mi nombre es Juan" y luego pregunta inmediatamente "¿Cuál es mi nombre?" El modelo no recordará porque cada mensaje es independiente. Esto demuestra el problema principal de la integración básica con modelos de lenguaje - sin contexto conversacional.

<img src="../../../translated_images/es/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo de Chat Sin Estado" width="800"/>

*La IA no recuerda tu nombre del mensaje anterior*

### Chat con Estado (Panel Derecho)

Ahora prueba la misma secuencia aquí. Di "Mi nombre es Juan" y luego "¿Cuál es mi nombre?" Esta vez lo recuerda. La diferencia es MessageWindowChatMemory - mantiene el historial de conversación e incluye eso en cada solicitud. Así es como funciona la IA conversacional en producción.

<img src="../../../translated_images/es/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo de Chat con Estado" width="800"/>

*La IA recuerda tu nombre de antes en la conversación*

Ambos paneles usan el mismo modelo GPT-5.2. La única diferencia es la memoria. Esto aclara qué aporta la memoria a tu aplicación y por qué es esencial para casos de uso reales.

## Próximos Pasos

**Próximo Módulo:** [02-prompt-engineering - Ingeniería de Prompts con GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegación:** [← Anterior: Módulo 00 - Inicio Rápido](../00-quick-start/README.md) | [Volver al Principal](../README.md) | [Siguiente: Módulo 02 - Ingeniería de Prompts →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por lograr precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional realizada por humanos. No nos hacemos responsables por malentendidos o interpretaciones erróneas derivadas del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->