<img src="../../translated_images/he/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 תמיכה בריבוי שפות

#### נתמך באמצעות פעולה של GitHub (אוטומטי ותמיד מעודכן)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ערבית](../ar/README.md) | [בנגלית](../bn/README.md) | [בולגרית](../bg/README.md) | [בורמזית (מיאנמר)](../my/README.md) | [סינית (מפושטת)](../zh-CN/README.md) | [סינית (מסורתית, הונג קונג)](../zh-HK/README.md) | [סינית (מסורתית, מקאו)](../zh-MO/README.md) | [סינית (מסורתית, טאיוואן)](../zh-TW/README.md) | [קרואטית](../hr/README.md) | [צ'כית](../cs/README.md) | [דנית](../da/README.md) | [הולנדית](../nl/README.md) | [אסטונית](../et/README.md) | [פינית](../fi/README.md) | [צרפתית](../fr/README.md) | [גרמנית](../de/README.md) | [יוונית](../el/README.md) | [עברית](./README.md) | [הינדי](../hi/README.md) | [הונגרית](../hu/README.md) | [אינדונזית](../id/README.md) | [איטלקית](../it/README.md) | [יפנית](../ja/README.md) | [קנדה](../kn/README.md) | [קוריאנית](../ko/README.md) | [ליטאית](../lt/README.md) | [מלאית](../ms/README.md) | [מאליאלם](../ml/README.md) | [מרטיהי](../mr/README.md) | [נפאלית](../ne/README.md) | [פידג'ין ניגרי](../pcm/README.md) | [נורווגית](../no/README.md) | [פרסית (פרסי)](../fa/README.md) | [פולנית](../pl/README.md) | [פורטוגזית (ברזיל)](../pt-BR/README.md) | [פורטוגזית (פורטוגל)](../pt-PT/README.md) | [פנג'אבי (גורמוכי)](../pa/README.md) | [רומנית](../ro/README.md) | [רוסית](../ru/README.md) | [סרבית (קירילית)](../sr/README.md) | [סלובקית](../sk/README.md) | [סלובנית](../sl/README.md) | [ספרדית](../es/README.md) | [סווהילית](../sw/README.md) | [שבדית](../sv/README.md) | [טגאלוג (פיליפינית)](../tl/README.md) | [טמילית](../ta/README.md) | [טלווגו](../te/README.md) | [תאית](../th/README.md) | [טורקית](../tr/README.md) | [אוקראינית](../uk/README.md) | [אורדו](../ur/README.md) | [וייטנאמית](../vi/README.md)

> **מעדיפים לשכפל באופן מקומי?**

> מאגר זה כולל למעלה מ-50 תרגומים בשפות שונות שמגדילים משמעותית את גודל ההורדה. לשכפול ללא תרגומים, השתמשו ב-sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> זה נותן לכם את כל מה שאתם צריכים כדי להשלים את הקורס במהירות הורדה גבוהה יותר.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j למתחילים

קורס לבניית יישומי AI עם LangChain4j ו-Azure OpenAI GPT-5, מצ'אט בסיסי לסוכני AI.

**חדש בלנגצ'יין4j?** עיינו ב-[מילון](docs/GLOSSARY.md) להגדרות מונחים ומושגים מרכזיים.

## תוכן העניינים

1. [התחלה מהירה](00-quick-start/README.md) - התחילו עם LangChain4j
2. [הקדמה](01-introduction/README.md) - למדו את יסודות LangChain4j
3. [הנדסת הפקודות](02-prompt-engineering/README.md) - למדו כיצד לעצב פקודות בצורה יעילה
4. [RAG (יצירה מוגברת במידע)](03-rag/README.md) - בניית מערכות חכמות מבוססות ידע
5. [כלים](04-tools/README.md) - שילוב כלים חיצוניים ועוזרים פשוטים
6. [MCP (פרוטוקול הקשר מודל)](05-mcp/README.md) - עבודה עם פרוטוקול הקשר המודל (MCP) ומודולי סוכנים
---

## מסלול הלמידה

> **התחלה מהירה**

1. פתחו מזך את המאגר לחשבון GitHub שלכם
2. לחצו על **Code** → לשונית **Codespaces** → **...** → **New with options...**
3. השתמשו בהגדרות ברירת מחדל – זה יבחר במיכל הפיתוח שנוצר עבור הקורס
4. לחצו על **Create codespace**
5. המתינו 5-10 דקות עד שהסביבה תהיה מוכנה
6. קפצו ישירות ל-[התחלה מהירה](./00-quick-start/README.md) כדי להתחיל!

לאחר השלמת המודולים, חקרו את [מדריך הבדיקות](docs/TESTING.md) כדי לראות את קונספטי הבדיקות של LangChain4j בפעולה.

> **הערה:** הדרכה זו משתמשת ב-GitHub Models וב-Azure OpenAI. מודול [התחלה מהירה](00-quick-start/README.md) משתמש ב-GitHub Models (אין צורך במנוי Azure), בעוד שמודולים 1-5 משתמשים ב-Azure OpenAI. התחילו עם [חשבון Azure חינמי](https://aka.ms/azure-free-account) אם אין לכם אחד.


## למידה עם GitHub Copilot

כדי להתחיל לתכנת במהירות, פתחו את הפרויקט בקודספייס GitHub או ב-IDE המקומי שלכם עם devcontainer המסופק. ה-devcontainer בשימוש בקורס זה מגיע עם תצורה מראש של GitHub Copilot לתכנות זוגי מבוסס AI.

כל דוגמת קוד כוללת שאלות מוצעות שתוכלו לשאול את GitHub Copilot כדי להעמיק את ההבנה שלכם. חפשו את הסימונים 💡/🤖 ב:

- **כותרות קבצי Java** - שאלות ספציפיות לכל דוגמה
- **קבצי README של המודולים** - שאלות לחקירה אחרי דוגמאות הקוד

**איך להשתמש:** פתחו כל קובץ קוד ושאלו את GitHub Copilot את השאלות המוצעות. יש לו הקשר מלא של בסיס הקוד ויכול להסביר, להרחיב ולהציע חלופות.

רוצים ללמוד יותר? עיינו ב-[Copilot לתכנות זוגי מבוסס AI](https://aka.ms/GitHubCopilotAI).


## משאבים נוספים

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j למתחילים](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js למתחילים](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / סוכנים
[![AZD למתחילים](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI למתחילים](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP למתחילים](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![סוכני AI למתחילים](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### סדרת AI גנרטיבי
[![AI גנרטיבי למתחילים](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI גנרטיבי (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![AI גנרטיבי (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![AI גנרטיבי (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### לימוד בסיסי
[![ML למתחילים](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![מדעי הנתונים למתחילים](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI למתחילים](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![אבטחת סייבר למתחילים](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![פיתוח רשת למתחילים](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT למתחילים](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![פיתוח XR למתחילים](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### סדרת קופילוט
[![קופילוט לתכנות משותף לבינה מלאכותית](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![קופילוט ל-C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![הרפתקת קופילוט](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- סיום הקורסים האחרים של מתרגם CO-OP -->

## קבלת עזרה

אם נתקעת או יש לך שאלות לגבי בניית אפליקציות בינה מלאכותית, הצטרף:

[![דיסקורד Azure AI Foundry](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

אם יש לך משוב על המוצר או שגיאות בעת הבנייה בקר ב:

[![פורום מפתחי Azure AI Foundry בגיטהאב](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## רישיון

רישיון MIT - ראה את הקובץ [LICENSE](../../LICENSE) לפרטים.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עשויים להכיל שגיאות או טעויות. המסמך המקורי בשפת המקור מהווה את המקור הסמכותי. למידע קריטי מומלץ תרגום מקצועי על ידי אדם. אנו לא נושאים באחריות לכל אי הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->