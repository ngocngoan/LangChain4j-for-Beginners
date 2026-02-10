# Infraestructura de Azure para LangChain4j Guía para Empezar

## Tabla de Contenidos

- [Prerequisitos](../../../../01-introduction/infra)
- [Arquitectura](../../../../01-introduction/infra)
- [Recursos Creados](../../../../01-introduction/infra)
- [Inicio Rápido](../../../../01-introduction/infra)
- [Configuración](../../../../01-introduction/infra)
- [Comandos de Gestión](../../../../01-introduction/infra)
- [Optimización de Costos](../../../../01-introduction/infra)
- [Monitoreo](../../../../01-introduction/infra)
- [Solución de Problemas](../../../../01-introduction/infra)
- [Actualización de Infraestructura](../../../../01-introduction/infra)
- [Limpieza](../../../../01-introduction/infra)
- [Estructura de Archivos](../../../../01-introduction/infra)
- [Recomendaciones de Seguridad](../../../../01-introduction/infra)
- [Recursos Adicionales](../../../../01-introduction/infra)

Este directorio contiene la infraestructura de Azure como código (IaC) utilizando Bicep y Azure Developer CLI (azd) para desplegar recursos de Azure OpenAI.

## Prerequisitos

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versión 2.50.0 o posterior)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versión 1.5.0 o posterior)
- Una suscripción de Azure con permisos para crear recursos

## Arquitectura

**Configuración Simplificada para Desarrollo Local** - Despliega solo Azure OpenAI, ejecuta todas las aplicaciones localmente.

La infraestructura despliega los siguientes recursos de Azure:

### Servicios de IA
- **Azure OpenAI**: Servicios Cognitivos con dos despliegues de modelos:
  - **gpt-5.2**: Modelo de completado de chat (capacidad 20K TPM)
  - **text-embedding-3-small**: Modelo de incrustación para RAG (capacidad 20K TPM)

### Desarrollo Local
Todas las aplicaciones Spring Boot se ejecutan localmente en tu máquina:
- 01-introduction (puerto 8080)
- 02-prompt-engineering (puerto 8083)
- 03-rag (puerto 8081)
- 04-tools (puerto 8084)

## Recursos Creados

| Tipo de Recurso | Patrón de Nombre de Recurso | Propósito |
|-----------------|-----------------------------|-----------|
| Grupo de Recursos | `rg-{environmentName}` | Contiene todos los recursos |
| Azure OpenAI | `aoai-{resourceToken}` | Hospedaje de los modelos de IA |

> **Nota:** `{resourceToken}` es una cadena única generada a partir del ID de suscripción, nombre del entorno y ubicación

## Inicio Rápido

### 1. Desplegar Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

Cuando se te solicite:
- Selecciona tu suscripción de Azure
- Elige una ubicación (recomendado: `eastus2` para disponibilidad de GPT-5.2)
- Confirma el nombre del entorno (por defecto: `langchain4j-dev`)

Esto creará:
- Recurso Azure OpenAI con GPT-5.2 y text-embedding-3-small
- Detalles de conexión de salida

### 2. Obtener Detalles de Conexión

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Esto muestra:
- `AZURE_OPENAI_ENDPOINT`: La URL del endpoint de Azure OpenAI
- `AZURE_OPENAI_KEY`: Clave API para autenticación
- `AZURE_OPENAI_DEPLOYMENT`: Nombre del modelo de chat (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Nombre del modelo de incrustación

### 3. Ejecutar las Aplicaciones Localmente

El comando `azd up` crea automáticamente un archivo `.env` en el directorio raíz con todas las variables de entorno necesarias.

**Recomendado:** Inicia todas las aplicaciones web:

**Bash:**
```bash
# Desde el directorio raíz
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Desde el directorio raíz
cd ../..
.\start-all.ps1
```

O inicia un módulo individual:

**Bash:**
```bash
# Ejemplo: Iniciar solo el módulo de introducción
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Ejemplo: Iniciar solo el módulo de introducción
cd ../01-introduction
.\start.ps1
```

Ambos scripts cargan automáticamente las variables de entorno del archivo `.env` raíz creado por `azd up`.

## Configuración

### Personalización de Despliegues de Modelos

Para cambiar los despliegues de modelos, edita `infra/main.bicep` y modifica el parámetro `openAiDeployments`:

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

Modelos y versiones disponibles: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Cambiar Regiones de Azure

Para desplegar en una región diferente, edita `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Consulta la disponibilidad de GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Para actualizar la infraestructura tras hacer cambios en los archivos Bicep:

**Bash:**
```bash
# Reconstruir la plantilla ARM
az bicep build --file infra/main.bicep

# Vista previa de los cambios
azd provision --preview

# Aplicar cambios
azd provision
```

**PowerShell:**
```powershell
# Reconstruir la plantilla ARM
az bicep build --file infra/main.bicep

# Previsualizar cambios
azd provision --preview

# Aplicar cambios
azd provision
```

## Limpieza

Para eliminar todos los recursos:

**Bash:**
```bash
# Eliminar todos los recursos
azd down

# Eliminar todo, incluido el entorno
azd down --purge
```

**PowerShell:**
```powershell
# Eliminar todos los recursos
azd down

# Eliminar todo, incluido el entorno
azd down --purge
```

**Advertencia**: Esto eliminará permanentemente todos los recursos de Azure.

## Estructura de Archivos

## Optimización de Costos

### Desarrollo/Pruebas
Para entornos de desarrollo/prueba, puedes reducir costos:
- Usa el nivel Standard (S0) para Azure OpenAI
- Configura una capacidad menor (10K TPM en lugar de 20K) en `infra/core/ai/cognitiveservices.bicep`
- Elimina recursos cuando no los uses: `azd down`

### Producción
Para producción:
- Incrementa la capacidad de OpenAI según el uso (50K+ TPM)
- Habilita redundancia de zona para mayor disponibilidad
- Implementa monitoreo adecuado y alertas de costo

### Estimación de Costos
- Azure OpenAI: Pago por token (entrada + salida)
- GPT-5.2: ~$3-5 por 1M de tokens (ver precios actuales)
- text-embedding-3-small: ~$0.02 por 1M de tokens

Calculadora de precios: https://azure.microsoft.com/pricing/calculator/

## Monitoreo

### Ver Métricas de Azure OpenAI

Ve al Portal de Azure → Tu recurso OpenAI → Métricas:
- Utilización basada en tokens
- Tasa de solicitudes HTTP
- Tiempo de respuesta
- Tokens activos

## Solución de Problemas

### Problema: Conflicto de nombre de subdominio en Azure OpenAI

**Mensaje de Error:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Causa:**
El nombre del subdominio generado a partir de tu suscripción/entorno ya está en uso, posiblemente por un despliegue previo que no se eliminó completamente.

**Solución:**
1. **Opción 1 - Usa un nombre de entorno diferente:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **Opción 2 - Despliegue manual vía Portal de Azure:**
   - Ve al Portal de Azure → Crear un recurso → Azure OpenAI
   - Elige un nombre único para tu recurso
   - Despliega los siguientes modelos:
     - **GPT-5.2**
     - **text-embedding-3-small** (para módulos RAG)
   - **Importante:** Anota los nombres de tus despliegues - deben coincidir con la configuración de `.env`
   - Tras el despliegue, obtén el endpoint y la clave API en "Claves y Endpoint"
   - Crea un archivo `.env` en la raíz del proyecto con:
     
     **Ejemplo de archivo `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Guías para nombrar despliegues de modelos:**
- Usa nombres simples y consistentes: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Los nombres de despliegue deben coincidir exactamente con lo configurado en `.env`
- Error común: Crear un modelo con un nombre y referenciar otro diferente en el código

### Problema: GPT-5.2 no disponible en la región seleccionada

**Solución:**
- Elige una región con acceso a GPT-5.2 (ej.: eastus2)
- Consulta disponibilidad: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Problema: Cupo insuficiente para despliegue

**Solución:**
1. Solicita aumento de cuota en el Portal de Azure
2. O usa una capacidad menor en `main.bicep` (ej., capacity: 10)

### Problema: "Recurso no encontrado" al ejecutar localmente

**Solución:**
1. Verifica el despliegue: `azd env get-values`
2. Revisa que el endpoint y la clave sean correctos
3. Asegúrate de que el grupo de recursos exista en el Portal de Azure

### Problema: Fallo de autenticación

**Solución:**
- Verifica que `AZURE_OPENAI_API_KEY` esté correctamente configurado
- El formato de la clave debe ser una cadena hexadecimal de 32 caracteres
- Obtén una clave nueva desde el Portal de Azure si es necesario

### Fallo en Despliegue

**Problema**: `azd provision` falla con errores de cuota o capacidad

**Solución**: 
1. Prueba una región diferente - Ver sección [Cambiar Regiones de Azure](../../../../01-introduction/infra) para configurar regiones
2. Verifica que tu suscripción tenga cuota para Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### La Aplicación No Se Conecta

**Problema**: La aplicación Java muestra errores de conexión

**Solución**:
1. Verifica que las variables de entorno estén exportadas:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. Confirma que el formato del endpoint sea correcto (debe ser `https://xxx.openai.azure.com`)
3. Asegura que la clave API sea la clave primaria o secundaria del Portal de Azure

**Problema**: 401 No autorizado desde Azure OpenAI

**Solución**:
1. Obtén una clave API nueva desde Portal de Azure → Claves y Endpoint
2. Reexporta la variable de entorno `AZURE_OPENAI_API_KEY`
3. Verifica que los despliegues de modelos estén completos (ver en Portal de Azure)

### Problemas de Rendimiento

**Problema**: Tiempo de respuesta lento

**Solución**:
1. Verifica el uso de tokens OpenAI y posibles limitaciones en las métricas del Portal de Azure
2. Incrementa la capacidad TPM si estás alcanzando límites
3. Considera usar un nivel superior de esfuerzo de razonamiento (bajo/medio/alto)

## Actualización de Infraestructura

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## Recomendaciones de Seguridad

1. **Nunca comprometas claves API** - Usa variables de entorno
2. **Usa archivos .env localmente** - Agrega `.env` a `.gitignore`
3. **Rota claves regularmente** - Genera nuevas claves en el Portal de Azure
4. **Limita accesos** - Usa RBAC de Azure para controlar acceso a recursos
5. **Monitorea uso** - Configura alertas de costos en el Portal de Azure

## Recursos Adicionales

- [Documentación del Servicio Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Documentación del Modelo GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Documentación de Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Documentación de Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Integración Oficial LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Soporte

Para problemas:
1. Revisa la [sección de solución de problemas](../../../../01-introduction/infra) arriba
2. Consulta el estado del servicio Azure OpenAI en el Portal de Azure
3. Abre un issue en el repositorio

## Licencia

Consulta el archivo raíz [LICENSE](../../../../LICENSE) para más detalles.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por la precisión, tenga en cuenta que las traducciones automáticas pueden contener errores o inexactitudes. El documento original en su idioma nativo debe considerarse la fuente autorizada. Para información crítica, se recomienda la traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones incorrectas que puedan surgir del uso de esta traducción.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->