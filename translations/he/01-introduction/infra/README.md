# תשתית Azure עבור LangChain4j התחלה מהירה

## תוכן העניינים

- [דרישות מוקדמות](../../../../01-introduction/infra)
- [ארכיטקטורה](../../../../01-introduction/infra)
- [משאבים שנוצרו](../../../../01-introduction/infra)
- [התחלה מהירה](../../../../01-introduction/infra)
- [הגדרות](../../../../01-introduction/infra)
- [פקודות ניהול](../../../../01-introduction/infra)
- [אופטימיזציה של עלויות](../../../../01-introduction/infra)
- [מעקב](../../../../01-introduction/infra)
- [פתרון בעיות](../../../../01-introduction/infra)
- [עדכון תשתית](../../../../01-introduction/infra)
- [ניקוי](../../../../01-introduction/infra)
- [מבנה קבצים](../../../../01-introduction/infra)
- [המלצות אבטחה](../../../../01-introduction/infra)
- [משאבים נוספים](../../../../01-introduction/infra)

תיקייה זו מכילה את תשתית Azure כקוד (IaC) באמצעות Bicep ו-Azure Developer CLI (azd) לפריסת משאבי Azure OpenAI.

## דרישות מוקדמות

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (גרסה 2.50.0 או מאוחרת יותר)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (גרסה 1.5.0 או מאוחרת יותר)
- מנוי Azure עם הרשאות ליצירת משאבים

## ארכיטקטורה

**הגדרת פיתוח מקומי מפושטת** - פרוס רק Azure OpenAI, הפעל את כל האפליקציות מקומית.

התשתית מפרסת את משאבי Azure הבאים:

### שירותי AI
- **Azure OpenAI**: שירותי קוגניציה עם שתי פריסות של מודלים:
  - **gpt-5.2**: מודל השלמת שיחה (קיבולת 20K TPM)
  - **text-embedding-3-small**: מודל הטמעה ל-RAG (קיבולת 20K TPM)

### פיתוח מקומי
כל יישומי Spring Boot פועלים מקומית במכונה שלך:
- 01-introduction (פורט 8080)
- 02-prompt-engineering (פורט 8083)
- 03-rag (פורט 8081)
- 04-tools (פורט 8084)

## משאבים שנוצרו

| סוג המשאב | תבנית שם המשאב | מטרה |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | מכיל את כל המשאבים |
| Azure OpenAI | `aoai-{resourceToken}` | אירוח מודל AI |

> **הערה:** `{resourceToken}` הוא מחרוזת ייחודית שנוצרה ממספר המנוי, שם הסביבה והמיקום

## התחלה מהירה

### 1. פרוס Azure OpenAI

**באש:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

בעת בקשה:
- בחר את מנוי ה-Azure שלך
- בחר מיקום (מומלץ: `eastus2` עבור זמינות GPT-5.2)
- אשר את שם הסביבה (ברירת מחדל: `langchain4j-dev`)

זה יוצר:
- משאב Azure OpenAI עם GPT-5.2 ו-text-embedding-3-small
- פרטי חיבור פלט

### 2. קבל פרטי חיבור

**באש:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

זה מציג:
- `AZURE_OPENAI_ENDPOINT`: כתובת הסיום של Azure OpenAI שלך
- `AZURE_OPENAI_KEY`: מפתח API לאימות
- `AZURE_OPENAI_DEPLOYMENT`: שם מודל השיחה (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: שם מודל ההטמעה

### 3. הפעל יישומים מקומית

הפקודה `azd up` יוצרת אוטומטית קובץ `.env` בספריית השורש עם כל משתני הסביבה הדרושים.

**מומלץ:** הפעל את כל יישומי הווב:

**באש:**
```bash
# מהספרייה השורשית
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# מספריית השורש
cd ../..
.\start-all.ps1
```

או הפעל מודול בודד:

**באש:**
```bash
# דוגמה: להתחיל רק את מודול המבוא
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# דוגמה: להתחיל רק את מודול ההקדמה
cd ../01-introduction
.\start.ps1
```

שני הסקריפטים טוענים אוטומטית את משתני הסביבה מקובץ `.env` בשורש שנוצר על ידי `azd up`.

## הגדרות

### התאמת פריסות מודל

כדי לשנות פריסות מודל, ערוך את `infra/main.bicep` ושנה את הפרמטר `openAiDeployments`:

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

מודלים זמינים וגרסאות: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### שינוי אזורי Azure

לפריסה באזור שונה, ערוך את `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

בדוק זמינות GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

כדי לעדכן את התשתית לאחר שינויים בקבצי Bicep:

**באש:**
```bash
# לבנות מחדש את תבנית ה-ARM
az bicep build --file infra/main.bicep

# הצגת שינויים
azd provision --preview

# החל שינויים
azd provision
```

**PowerShell:**
```powershell
# לבנות מחדש את תבנית ה-ARM
az bicep build --file infra/main.bicep

# תצוגה מקדימה של השינויים
azd provision --preview

# להחיל את השינויים
azd provision
```

## ניקוי

כדי למחוק את כל המשאבים:

**באש:**
```bash
# מחק את כל המשאבים
azd down

# מחק הכל כולל את הסביבה
azd down --purge
```

**PowerShell:**
```powershell
# מחק את כל המשאבים
azd down

# מחק הכל כולל הסביבה
azd down --purge
```

**אזהרה**: פעולה זו תמחק לצמיתות את כל משאבי Azure.

## מבנה קבצים

## אופטימיזציה של עלויות

### פיתוח/בדיקות
לסביבות פיתוח/בדיקה ניתן להפחית עלויות:
- השתמש ב-tier סטנדרטי (S0) ל-Azure OpenAI
- הגדר קיבולת נמוכה יותר (10K TPM במקום 20K) ב-`infra/core/ai/cognitiveservices.bicep`
- מחק משאבים כשלא בשימוש: `azd down`

### ייצור
לייצור:
- הגדל קיבולת OpenAI בהתאם לשימוש (50K+ TPM)
- אפשר אזורי זמינות כפולים לזמינות גבוהה יותר
- הטמע ניטור וערכת התראות עלויות נכונה

### הערכת עלויות
- Azure OpenAI: תשלום לפי טוקן (קלט + פלט)
- GPT-5.2: כ-3-5$ לכל מיליון טוקנים (בדוק מחירים עדכניים)
- text-embedding-3-small: כ-0.02$ לכל מיליון טוקנים

מחשב מחירים: https://azure.microsoft.com/pricing/calculator/

## מעקב

### צפייה במדדי Azure OpenAI

גש ל-Portal Azure → משאב OpenAI שלך → Metrics:
- שימוש מבוסס טוקנים
- קצב בקשות HTTP
- זמן תגובה
- טוקנים פעילים

## פתרון בעיות

### בעיה: שם תת-דומיין של Azure OpenAI בשימוש

**הודעת שגיאה:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**סיבה:**
שם תת התחום שנוצר מהמנוי/הסביבה שלך כבר בשימוש, ייתכן מיישום קודם שלא נמחק במלואו.

**פתרון:**
1. **אפשרות 1 - השתמש בשם סביבה שונה:**
   
   **באש:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **אפשרות 2 - פריסה ידנית דרך פורטל Azure:**
   - עבור ל-Portal Azure → צור משאב → Azure OpenAI
   - בחר שם ייחודי למשאב שלך
   - פרוס את המודלים הבאים:
     - **GPT-5.2**
     - **text-embedding-3-small** (לעמודי RAG)
   - **חשוב:** רשום את שמות הפריסה - הם חייבים להתאים להגדרות `.env`
   - לאחר הפריסה, קבל את נקודת הקצה והמפתח מתוך "Keys and Endpoint"
   - צור קובץ `.env` בשורש הפרויקט עם:
     
     **דוגמת קובץ `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**הנחיות לשמות פריסת מודלים:**
- השתמש בשמות פשוטים ועקביים: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- שמות הפריסה חייבים להתאים בדיוק למה שהוגדר ב-`.env`
- טעות שכיחה: יצירת מודל בשם אחד אך הפנייה בשם שונה בקוד

### בעיה: GPT-5.2 לא זמין באזור שנבחר

**פתרון:**
- בחר אזור עם גישה ל-GPT-5.2 (למשל eastus2)
- בדוק זמינות: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### בעיה: אין מספיק מכסה לפריסה

**פתרון:**
1. בקש הגדלת מכסה בפורטל Azure
2. או השתמש בקיבולת נמוכה יותר ב-`main.bicep` (למשל קיבולת: 10)

### בעיה: "Resource not found" בעת הפעלה מקומית

**פתרון:**
1. אמת פריסה: `azd env get-values`
2. וודא שנקודת הקצה והמפתח נכונים
3. וודא שקבוצת המשאבים קיימת בפורטל Azure

### בעיה: אימות נכשל

**פתרון:**
- אמת ש-`AZURE_OPENAI_API_KEY` מוגדר נכון
- מפתח בפורמט מחרוזת הקסה-דצימלית באורך 32 תווים
- קבל מפתח חדש מפורטל Azure במידת הצורך

### הפריסה נכשלת

**בעיה**: `azd provision` נכשל בשגיאות מכסה או קיבולת

**פתרון**: 
1. נסה אזור שונה - ראה [שינוי אזורי Azure](../../../../01-introduction/infra)
2. בדוק שמנוי Azure שלך כולל מכסת Azure OpenAI:
   
   **באש:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### היישום לא מתחבר

**בעיה**: יישום Java מציג שגיאות חיבור

**פתרון**:
1. אמת שייצוא משתני הסביבה בוצע:
   
   **באש:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. בדוק שנקודת הקצה בפורמט נכון (צריכה להיות `https://xxx.openai.azure.com`)
3. אמת שמפתח ה-API הוא הראשי או המשני מפורטל Azure

**בעיה**: 401 Unauthorized מ-Azure OpenAI

**פתרון**:
1. קבל מפתח API חדש מ-Portal Azure → Keys and Endpoint
2. ייצא מחדש את משתנה הסביבה `AZURE_OPENAI_API_KEY`
3. ודא שפריסות המודל הושלמו (בדוק בפורטל Azure)

### בעיות ביצועים

**בעיה**: זמני תגובה איטיים

**פתרון**:
1. בדוק שימוש בטוקנים וחסימות ב-Metrics של פורטל Azure
2. הגדל קיבולת TPM אם מגיעים למגבלות
3. שקול שימוש ברמת מאמץ חשיבה גבוהה יותר (נמוך/בינוני/גבוה)

## עדכון תשתית

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

## המלצות אבטחה

1. **לעולם אל תכלול מפתחות API בקומיטים** - השתמש במשתני סביבה
2. **השתמש בקבצי .env מקומית** - הוסף `.env` ל-`.gitignore`
3. **סובב מפתחות באופן קבוע** - צור מפתחות חדשים בפורטל Azure
4. **הגבל גישה** - השתמש ב-Azure RBAC לשליטה בגישה למשאבים
5. **נטר שימוש** - הגדר התראות עלויות בפורטל Azure

## משאבים נוספים

- [תיעוד שירות Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [תיעוד מודל GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [תיעוד Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [תיעוד Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [שילוב רשמי של LangChain4j עם OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## תמיכה

לבעיות:
1. בדוק את [מדור פתרון הבעיות](../../../../01-introduction/infra) למעלה
2. סקור את מצב שירות Azure OpenAI בפורטל Azure
3. פתח פנייה בריפוזיטורי

## רישיון

עיין בקובץ [LICENSE](../../../../LICENSE) בשורש לפרטים.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור:**
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להעדיף את המסמך המקורי בשפתו המקורית כמקור הסמכות. למידע קריטי מומלץ לפנות לתרגום מקצועי אנושי. איננו אחראים לכל אי הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->