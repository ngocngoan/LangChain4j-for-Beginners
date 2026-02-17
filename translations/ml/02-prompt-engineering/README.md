# Module 02: GPT-5.2 ഉപയോഗിച്ച് പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്

## Table of Contents

- [What You'll Learn](../../../02-prompt-engineering)
- [Prerequisites](../../../02-prompt-engineering)
- [Understanding Prompt Engineering](../../../02-prompt-engineering)
- [Prompt Engineering Fundamentals](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Advanced Patterns](../../../02-prompt-engineering)
- [Using Existing Azure Resources](../../../02-prompt-engineering)
- [Application Screenshots](../../../02-prompt-engineering)
- [Exploring the Patterns](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [What You're Really Learning](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## What You'll Learn

<img src="../../../translated_images/ml/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

മുൻ മൊഡ്യൂളിൽ, സംഭാഷണ മേഖലയിൽ മെമ്മറി എങ്ങനെ പ്രവർത്തിക്കുന്നുവെന്ന് നിങ്ങൾ കണ്ടു, കൂടാതെ GitHub മോഡലുകൾ അടിസ്ഥാന ഇന്ററാക്ഷനുകൾക്കായി ഉപയോഗിച്ചു. ഇപ്പോൾ നാം Azure OpenAI യുടെ GPT-5.2 ഉപയോഗിച്ച് നിങ്ങൾ ചോദിക്കുന്ന ചോദ്യങ്ങൾ — പ്രോംപ്റ്റുകൾ തന്നെ — എങ്ങനെ രൂപകൽപ്പന ചെയ്യാമെന്ന് ശ്രദ്ധിക്കുകയാണ്. നിങ്ങൾ പ്രോംപ്റ്റുകൾ എങ്ങനെ ഘടിപ്പിക്കുന്നുവോ, ലഭിക്കുന്ന മറുപടികളുടെ ഗുണത്തിന്റെ മേൽ അത് ഭരിക്കുന്നു. പ്രധാന പ്രോംപ്റ്റിംഗ് സാങ്കেতിക വിദ്യകളുടെ അവലോകനത്തോടെ തുടക്കം വച്ച്, പിന്നീട് ഞങ്ങൾ GPT-5.2-യുടെ ശേഷികളെ പൂര്‍ണമായും ഉപയോഗിക്കുന്ന എട്ട് ആധുനിക പാറ്റേണുകൾക്ക് കടക്കുന്നു.

Reasoning control കമ്പിളി കൊണ്ടുള്ള GPT-5.2 ഞങ്ങൾ ഉപയോഗിക്കും - നിങ്ങൾ മോഡലിനോട് ആലോചന എത്രത്തോളം വേണമെന്നു പറയാം. ഇത് വിവിധ പ്രോംപ്റ്റ് തന്ത്രങ്ങൾ കൂടുതൽ വ്യക്തമാക്കുകയും ഓരോ സമീപനം എപ്പോഴാണ് ഉപയോഗിക്കേണ്ടതെന്നും മനസ്സിലാക്കാൻ സഹായിക്കുന്നു. GitHub മോഡലുകളേക്കാൾ Azure-ൽ GPT-5.2-നുള്ള കുറവ് നിരക്ക് പരിധികൾ കൂടി ഫലം നൽകും.

## Prerequisites

- Module 01 (Azure OpenAI റിസോഴ്‌സുകൾ പ്രവർത്തനക്ഷമമാക്കി) പൂർത്തിയാക്കിയിരിക്കണം
- റൂട്ട് ഡയറക്ടറിയിൽ `.env` ഫയൽ Azure ക്രെഡൻഷ്യലുകളോടെ (Module 01-ൽ `azd up` ന് കൃത്യമായ ഫയൽ)

> **പരാമർശം:** Module 01 പൂർത്തിയാക്കിയിട്ടില്ലെങ്കിൽ, ആദ്യം അവിടെ നൽകിയ ഡിപ്പ്ലോയ്മെന്റ് നിർദ്ദേശങ്ങൾ പിന്തുടരുക.

## Understanding Prompt Engineering

<img src="../../../translated_images/ml/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ് അതായത് നിങ്ങൾക്ക് ആവശ്യമായ ഫലം സ്ഥിരമായി എടുക്കാൻ കഴിയുന്ന ഇൻപുട്ട് ടെക്സ്റ്റ് രൂപകൽപ്പന ചെയ്യുകയാണ്. ഇത് വെറും ചോദ്യങ്ങൾ ചോദിക്കുന്നതല്ല - മോഡൽ ഏത് കാര്യങ്ങൾ ഞങ്ങളുടെ അപേക്ഷയിൽ മനസ്സിലാക്കണമെന്നും അത് എങ്ങനെ കൈകാര്യം ചെയ്യണമെന്നുമുള്ള നിർദ്ദേശങ്ങൾ മിതമായി ഘടിപ്പിക്കുന്നതാണ്.

ഇത് ഒരു സഹപ്രവർത്തകനെ നിർദ്ദേശങ്ങൾ നൽകുന്നതുപോലെയാണ്. "ബഗ് ശരിയാക്കുക" എന്നത് പ്രസക്തമല്ല. "UserService.java ഫയലിലെ 45-ാം വരിയിൽ നൾ പോയിന്റർ_EXCEPTION ശരിയാക്കുക‌ എന്നതുപോലെ ഒറ്റനൽകിയ നിർദ്ദേശം" കൂടുതലായി വ്യക്തമാക്കുന്നു. ഭാഷാ മോഡലുകളും അവേ പശ്ചാത്തലത്തിലുള്ള വ്യക്തതയും ഘടനാപരമായ ശുദ്ധിയും അവഗണിക്കരുത്.

<img src="../../../translated_images/ml/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ആധാരസൗകര്യങ്ങളുടെ — മോഡൽ കണക്ഷനുകൾ, മെമ്മറി, സന്ദേശ തരങ്ങൾ — ഒരുക്കം നൽകുന്നു, അതിന്റെ മാജിക്ക് പാറ്റേണുകൾ കൃത്യമായി ഘടിപ്പിച്ച ടെക്സ്റ്റ് മാത്രമാണ്. പ്രധാന ഘടകങ്ങൾ `SystemMessage` (AI-യുടെ പെരുമാറ്റവും റോളും സജ്ജീകരിക്കുന്നു) மற்றும் `UserMessage` (നിങ്ങളുടെ യഥാർത്ഥ അഭ്യർത്ഥന അടങ്ങിയിരിക്കുന്നു).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/ml/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ഈ മൊഡ്യൂളിലെ ആധുനിക പാറ്റേണുകൾക്കു മുന്‍പായി, നാം അഞ്ചു അടിസ്ഥാന പ്രോംപ്റ്റിംഗ് സാങ്കേതിക ദിശകൾ സൂചനാപ്രദമായ ഒരു അവലോകനം നടത്താം. ഓരോ പ്രോംപ്റ്റ് എഞ്ചിനീയറുമറിയേണ്ട സ്ഥാപിത അരിപ്പുകളാണ് ഇവ. നിങ്ങൾ [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) അയാളിൽ ഇവ കാണിച്ചിട്ടുണ്ട് — ഇവയുടെ ആശയ ഘടന താഴെക്കാണുന്നു.

### Zero-Shot Prompting

സരളമായ സമീപനം: ഉദാഹരണങ്ങളില്ലാതെ നേരിട്ട് നിർദ്ദേശം നൽകുക. മോഡൽ തങ്ങളുടെ പരിശീലനത്തിൽ മാത്രം ആശ്രയിച്ച് ടാസ്ക് മനസ്സിലാക്കി അതെപ്രകാരം ചെയ്തു കൊടുക്കും. പ്രവണതകള്‍ നേരിയവായിട്ടുള്ള ടാസ്ക്കൾക്കായി ഇത് നല്ലതാണ്.

<img src="../../../translated_images/ml/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*ഉദാഹരണങ്ങൾ കൂടാത്ത നേരിട്ടുള്ള നിർദ്ദേശം — മോഡൽ നിർദ്ദേശം മാത്രം നോക്കി ടാസ്ക് തിരിച്ചറിയുന്നു*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// പ്രതികരണം: "സ pozitive"
```

**എപ്പോൾ ഉപയോഗിക്കണം:** ലളിതമായ വർഗ്ഗീകരണങ്ങൾ, നേരിട്ടുള്ള ചോദ്യങ്ങൾ, തർജുമകൾ, അല്ലെങ്കിൽ കൂടുതൽ മാർഗ്ഗനിർദ്ദേശം ആവശ്യമില്ലാത്ത ടാസ്കുകൾ.

### Few-Shot Prompting

നീங்கள் മോഡലിന് പിന്തുടരണമുള്ള പാറ്റേണുകൾ കാണിക്കുന്ന ഉദാഹരണങ്ങൾ നൽകുക. ഉദാഹരണങ്ങളിൽ നിന്നാണ് മോഡൽ പ്രതീക്ഷിക്കുന്ന ഇൻപുട്ട്-ഔട്പുട്ട് ഫോർമാറ്റ് പഠിക്കുന്നത്, ഇത് പുതിയவற்றിലേക്കും ബാധകം ആക്കി പ്രയോഗിക്കുന്നു. ഇതു ആഗ്രഹിക്കുന്ന ഫോർമാറ്റ് അല്ലെങ്കിൽ പെരുമാറ്റം വ്യക്തമല്ലാത്ത ടാസ്കുകളിൽ ഒട്ടനവധി മത്സരത്വം മെച്ചപ്പെടുത്തുന്നു.

<img src="../../../translated_images/ml/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*ഉദാഹരണങ്ങളിൽനിന്ന് പഠനം — മോഡൽ പാറ്റേണും പുതിയ ഇൻപുട്ടുകളിൽ പ്രയോഗിച്ചുകൊണ്ടിരിക്കുന്നു*

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

**എപ്പോൾ ഉപയോഗിക്കണം:**ാന സന്ദർഭങ്ങളിൽ കസ്റ്റം വർഗ്ഗീകരണങ്ങൾ, സ്ഥിരതയുള്ള ഫോർമാറ്റിംഗ്, ഡൊമെയ്ൻ-സ്പെസിഫിക് ജോലികൾ, അല്ലെങ്കിൽ zero-shot ഫലം സ്ഥിരത ഇല്ലാതിരിക്കാൻ.

### Chain of Thought

മോഡലിനെ ചുവടെയുള്ള ഒന്നൊന്നായി ആലോചന പ്രകടിപ്പിക്കാൻ ആവശ്യപ്പെടുക. ഒരു ഉത്തരത്തിലേക്ക് ഉടൻ ചാടാതെ, മോഡൽ പ്രശ്നം വിറളിക്കരുതി ഓരോ ഘടകവും തുറന്നുറപ്പിക്കുന്നു. ഇത് ഗണിതം, ലജിക്, പലവട്ട ആലോചന വേർതിരിച്ചെടുക്കൽ ടാസ്കുകളിൽ കൃത്യത മെച്ചപ്പെടുത്തുന്നു.

<img src="../../../translated_images/ml/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*ഓരോഘട്ടവും തുറന്നുള്ള ആലോചന - സങ്കീർണ്ണ പ്രശ്നങ്ങളെ ലജിക്കൽ ഘട്ടങ്ങളാക്കി ഭേദിച്ചു കാണിക്കുന്നു*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// മോഡൽ കാണിക്കുന്നത്: 15 - 8 = 7, പിന്നീട് 7 + 12 = 19 ആപ്പിൾസ്
```

**എപ്പോൾ ഉപയോഗിക്കണം:** ഗണിത പ്രശ്നങ്ങൾ, ലജിക് പസിലുകൾ, ഡീബഗിംഗ്, അല്ലെങ്കിൽ ആലോചന പ്രക്രിയ കാണിക്കുന്നതു കൃത്യതയും വിശ്വാസ്യതയും മെച്ചപ്പെടുത്തുന്നതിന്.

### Role-Based Prompting

നീങ്ങളുടെ ചോദ്യത്തിന് മുമ്പ് AI-യെ ഒരു വ്യക്തിത്വമോ റോളോ നിയോഗിക്കുക. ഇത് മറുപടിയുടെ ശൈലി, ആഴം, ശ്രദ്ധ സ്വഭാവം സജ്ജീകരിക്കുന്നു. "സോഫ്റ്റ്വെയർ ആർക്കിടെക്റ്റ്" "ജൂനിയർ ഡെവലപ്പർ" അല്ലെങ്കിൽ "സിക്യുറിറ്റി ഓഡിറ്റർ" എന്നിവരെ അപേക്ഷിച്ച് വ്യത്യസ്ത ഉപദേശം നൽകും.

<img src="../../../translated_images/ml/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*സന്ദർഭവും വ്യക്തിത്വവും കളങ്കിക്കുന്നു — അവകാശപ്പെട്ട റോളിനനുസരിച്ച് ഒരേ ചോദ്യത്തിന് വ്യത്യസ്ത മറുപടി*

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

**എപ്പോൾ ഉപയോഗിക്കണം:** കോഡ് റിവ്യൂസ്, ട്യൂട്ടറിംഗ്, ഡൊമെയ്ൻ-സ്പെസിഫിക് വിശകലനം, അല്ലെങ്കിൽ പ്രത്യേക വിദഗ്ധതാ നില ഉൾപ്പെടെയുള്ള മറുപടികൾ ആവശ്യമായപ്പോൾ.

### Prompt Templates

പരിവർത്തനീയമായ പ്ലേസ്ഹോൾഡറുകൾ ഉപയോഗിച്ച് പുനരുപയോഗപ്രാപ്ത പ്രോംപ്റ്റുകൾ രൂപപ്പെടുത്തുക. ഓരോ പ്രാരംഭത്തിലും പുത്തൻ പ്രോംപ്റ്റ് എഴുതുന്ന പകരം, ഒന്ന് മാത്രം ടെംപ്ലേറ്റ് നിർവ്വചിച്ച് വ്യത്യസ്ത മൂല്യങ്ങൾ പൂരിപ്പിക്കുക. LangChain4j-യുടെ `PromptTemplate` ക്ലാസ് ഈ പ്രവൃത്തി `{{variable}}` സിങ്ക്സുമായി എളുപ്പമാക്കുന്നു.

<img src="../../../translated_images/ml/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*പരിവർത്തനീയമായ പ്ലേസ്ഹോൾഡറുകളുള്ള പുനരുപയോഗ പ്രോംപ്റ്റുകൾ — ഒന്ന് ടെംപ്ലേറ്റ്, അനേകം ഉപയോഗങ്ങൾ*

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

**എപ്പോൾ ഉപയോഗിക്കണം:** വ്യത്യസ്ത ഇൻപുട്ടുകളോടെ ആവർത്തിക്കുന്ന അന്വേഷണങ്ങൾ, ബാച്ച് പ്രോസസ്സിംഗ്, പുനരുപയോഗയോഗ്യ AI വർക്ക് ഫ്ലോകൾ, അല്ലെങ്കിൽ പ്രോംപ്റ്റ് ഘടന സ്ഥിരമാണെങ്കിലും ഡാറ്റ മാറുന്ന സന്നിവേശങ്ങൾ.

---

ഈ അഞ്ചു അടിസ്ഥാനങ്ങൾ നിങ്ങൾക്ക് കൂടുതലായി പ്രോംപ്റ്റ് ടാസ്കുകൾക്കായി ഒരു ദൃഢമായ ഉപകരണസഞ്ചയം നൽകുന്നു. ഈ മൊഡ്യൂളിന്റെ ബാക്കി ഭാഗം GPT-5.2യുടെ reasoning control, സ്വയം മൂല്യനിർണ്ണയം, ഘടിത ഔട്ട്പുട്ട് കഴിവുകൾ പ്രയോജനപ്പെടുത്തി ഉയർന്ന ആധുനിക പാറ്റേണുകളിലൂടെ മുന്നോട്ട് പോകുന്നു.

## Advanced Patterns

അടിസ്ഥാനങ്ങൾ പൂർത്തിയാക്കിയപ്പോൾ, ഈ മൊഡ്യൂളിനെ പ്രത്യേകമാക്കുന്ന എട്ട് ആധുനിക പാറ്റേണുകളിലേക്ക് നീങ്ങാം. എല്ലാ പ്രശ്നങ്ങൾക്കും ഒരേ സമീപനം വേണ്ട എന്ന്ല്ല. ചില ചോദ്യങ്ങൾ പെട്ടെന്ന് ഉത്തരങ്ങൾ വേണം, ചിലത് ഊളം ആലോചന വേണം. ചിലത് തെളിയിച്ച ആലോചന ആവശ്യമുള്ളതാണ്, ചിലത് ഫലങ്ങൾ മാത്രം ആവശ്യമുണ്ട്. താഴെ വെച്ചിരിക്കുന്ന ഓരോ പാറ്റേണും ഒരു വ്യത്യസ്ത സാഹചര്യത്തിന് അനുയോജ്യമാണ് — GPT-5.2_REASONING_CONTROL ആ വ്യത്യാസങ്ങൾ കൂടുതൽ വ്യക്തമാക്കുന്നു.

<img src="../../../translated_images/ml/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*എട്ട് പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ് പാറ്റേണുകളുടെ അവലോകനംയും ഇവ ഉപയോഗിക്കുന്ന അവസരങ്ങൾ*

<img src="../../../translated_images/ml/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2യുടെ reasoning control മോഡലിന് എത്രത്തോളം ആലോചന നടത്തണമെന്നും, പെട്ടെന്ന് നേരിട്ട് ഉത്തരമോ, ആഴത്തിൽ പരിശോധനയോ എന്നതും നിശ്ചയിക്കാം*

<img src="../../../translated_images/ml/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*കുറഞ്ഞ ആഗ്രഹം (പെട്ടെന്ന്, നേരിട്ട്) vs ഉയർന്ന ആഗ്രഹം (വ്യക്തമായി, വിശകലനപരമായി) ആയ ആലോചന സമീപനങ്ങൾ*

**Low Eagerness (Quick & Focused)** - ലളിതമായ ചോദ്യങ്ങൾക്കായി പെട്ടെന്ന് നിഷ്പക്ഷമായ ഉത്തരങ്ങൾ വേണ്ടപ്പോൾ. മോഡൽ യാതൊരു reasoning കൂടാതെ പരമാവധി 2 ഘട്ടം വരെ മാത്രം നടത്തും. കണക്കുകൂട്ടൽ, ലുക്കപ്പ്, അല്ലെങ്കിൽ സൂക്ഷ്മരഹിതമായ ചോദ്യങ്ങൾക്ക് ഇതുപയോഗിക്കുക.

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

> 💡 **GitHub Copilot ഉപയോഗിച്ച് അന്വേഷിക്കുക:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) തുറന്ന് ചോദിക്കുക:
> - "കുറഞ്ഞ ആഗ്രഹവും ഉയർന്ന ആഗ്രഹവും പ്രോംപ്റ്റിംഗ് പാറ്റേണുകളിൽ有什么 വ്യത്യാസങ്ങളുണ്ട്?"
> - "പ്രോംപ്റ്റുകളിൽ XML ടാഗുകൾ AI-യുടെ മറുപടി ഘടിപ്പിക്കുന്നതിൽ എങ്ങനെ സഹായിക്കുന്നു?"
> - "സ്വയം-പരിശോധന പാറ്റേണുകൾ എപ്പോൾ നേരിട്ടുള്ള നിർദ്ദേശവുമായി താരതമ്യം ചെയ്യപ്പെടുന്നു?"

**High Eagerness (Deep & Thorough)** - സങ്കീർണ്ണ പ്രശ്നങ്ങളിൽ സമഗ്രമായ വിശകലനം ആവശ്യമായപ്പോൾ. മോഡൽ ആഴത്തിൽ പരിശോധിച്ച് ആലോചന സ്പഷ്ടപെടുത്തുന്നു. സിസ്റ്റം ഡിസൈൻ, ആർക്കിടെക്ചർ തീരുമാനങ്ങൾ, സങ്കീർണ്ണ ഗവേഷണത്തിന് ഉപയോഗിക്കുക.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - പലഘട്ട പ്രവർത്തനങ്ങൾക്കായി. മോഡൽ ആദ്യത്തേത് ഒരു പദ്ധതി നൽകുന്നു, പ്രവർത്തനങ്ങളോടെ ഓരോ ഘട്ടവും വിവരിക്കുകയും, ഒടുവിൽ സംക്ഷേപം നൽകുകയും ചെയ്യുന്നു. മൈഗ്രേഷനുകൾ, നടപ്പാക്കലുകൾ, അല്ലെങ്കിൽ ഏത് പലഘട്ടപ്രക്രിയയും ഇവിടെ ഉൾപ്പെടുന്നു.

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

Chain-of-Thought prompting മോഡലിനോട് reasoning പ്രക്രിയ കാണിക്കണമെന്നു പ്രത്യേകം ആവശ്യപ്പെടുന്നു, സങ്കീർണ്ണ ടാസ്കുകളിൽ കൃത്യത മെച്ചപ്പെടുത്തുന്നു. ഘട്ടം-ഘട്ടം വിഭജനം മനുഷ്യരും AI-യും ലജിക് മനസ്സിലാക്കാൻ സഹായിക്കുന്നു.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റിൽ പരീക്ഷിക്കുക:** ഈ പാറ്റേണിനെ കുറിച്ച് ചോദിക്കുക:
> - "ദീർഘകാല പ്രവർത്തനങ്ങൾക്ക് ടെസ്‌ക്ക് എക്സിക്യൂഷൻ പാറ്റേൺ എങ്ങനെ ബാധ്യസ്ഥമാക്കാം?"
> - "ഉൽപ്പാദന ആപ്ലിക്കേഷനുകളിൽ ടൂൾ പ്രീആംബിളുകൾ ഘടിപ്പിക്കുന്നതിന് മികച്ച മാർഗ്ഗങ്ങൾ എന്തെല്ലാം?"
> - "ഒരു UI-യിൽ ഇടക്കാല പുരോഗതി അപ്ഡേറ്റുകൾ എങ്ങനെ പിടിച്ചു പ്രദർശിപ്പിക്കാം?"

<img src="../../../translated_images/ml/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*പദ്ധതി → നടപ്പാക്കൽ → സംക്ഷേപം workflow for multi-step tasks*

**Self-Reflecting Code** - ഉത്പാദന നിലവാരമുള്ള കോഡ് സൃഷ്ടിക്കാൻ. മോഡൽ ഉത്പാദന സ്റ്റാൻഡേർഡുകൾ പാലിച്ച് പൂർണ്ണമായ പിശക് കൈകാര്യം کولو കോഡ് രചിക്കുന്നു. പുതിയ ഫീച്ചറുകളും സേവനങ്ങളും നിർമ്മിക്കുന്നപ്പോൾ ഇത് ഉപയോഗിക്കുക.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ml/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*പുനരാവൃതമായ മെച്ചപ്പെടുത്തൽ ചക്രം - സൃഷ്ടിക്കുക, മൂല്യനിർണ്ണയം നടത്തുക, പ്രശ്നങ്ങൾ കണ്ടെത്തുക, മെച്ചപ്പെടുത്തുക, ആവർത്തിക്കുക*

**Structured Analysis** - സ്ഥിരമായ മൂല്യനിർണ്ണയത്തിനായി. മോഡൽ ഒരു നിശ്ചിത ഫ്രെയിംവർക്കിൽ കോഡ് അവലോകനം ചെയ്യുന്നു (ശ ár the, പ്രാക്ടിസുകൾ, പ്രകടനം, സുരക്ഷ, പരിപാല്യത). കോഡ് റിവ്യൂസ് അല്ലെങ്കിൽ ഗുണനിലവാര വിലയിരുത്തലുകൾക്കായി ഇത് ഉപയോഗിക്കുക.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റിൽ പരീക്ഷിക്കുക:** ഘടിത വിശകലനം സംബന്ധിച്ച് ചോദിക്കുക:
> - "വിവിധ കോൺടെക്സ്റ്റുകളിൽ വിശകലന ഫ്രെയിംവർക്കുകൾ എങ്ങനെ ഇഷ്‌ടാനുസരണമാക്കാം?"
> - "ഘടിത ഔട്ട്പുട്ട് പ്രോഗ്രാമാറ്റിക്കായി పార്സ് ചെയ്ത് പ്രവർത്തിക്കാൻ ഏറ്റവും നല്ല മാർഗ്ഗങ്ങളെട?"
> - "വിവിധ റിവ്യൂ സെഷനുകളിൽ സമാനമായ ഗുരുത്വതര നിലവാരങ്ങൾ ഉറപ്പാക്കാൻ എങ്ങനെ?"

<img src="../../../translated_images/ml/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*സ്ഥിരതയുള്ള കോഡ് റിവ്യൂകൾക്കായി ഫ്രെയിംവർക്കും ഗുരുത്വനിലകളും*

**Multi-Turn Chat** - ബന്ധപ്പെട്ട സംഭാഷണങ്ങൾക്ക്. മോഡൽ മുമ്പത്തെ സന്ദേശങ്ങൾ ഓർക്കുകയും അവയ്ക്ക് അധിഷ്ഠിതമായി മറുപടി നൽകുകയും ചെയ്യുന്നു. ഇന്ററാക്ടീവ് സഹായ സെഷനുകൾ അല്ലെങ്കിൽ സങ്കീർണ്ണ ചോദ്യോത്തരങ്ങൾക്ക് ഉപയോഗിക്കുക.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ml/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*ചുരുങ്ങിയ ടോക്കൺ പരിധിയിലേക്കു മുൻപരിചയ സംഭാഷണ സന്ദേശങ്ങൾ എങ്ങനെ സഞ്ചയിക്കുന്നു*

**Step-by-Step Reasoning** - ദൃശ്യമായ ലജിക് ആവശ്യമായ പ്രശ്നങ്ങൾക്കായി. മോഡൽ ഓരോ ഘട്ടത്തിനും തുറന്ന ആലോചന കാണിക്കുന്നു. ഗണിത പ്രശ്നങ്ങൾ, ലജിക് പസിലുകൾ, അല്ലെങ്കിൽ ആലോചന പ്രക്രിയ മനസ്സിലാക്കേണ്ടപ്പോൾ ഇത് ഉപയോഗിക്കുക.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ml/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*പ്രശ്നങ്ങളെ ലജിക്കൽ ഘട്ടങ്ങളായി വിഭജിക്കുന്നത്*

**Constrained Output** - പ്രത്യേക ഫോർമാറ്റ് ആവശ്യകതകളുള്ള മറുപടികൾക്കായി. മോഡൽ ഫോർമാറ്റും നീളവും കർശനമായി പാലിക്കുന്നു. സംഗ്രഹങ്ങൾക്കായി അല്ലെങ്കിൽ കൃത്യമായ ഔട്ട്പുട്ട് ഘടന വേണ്ടപ്പോൾ ഇത് ഉപയോഗിക്കുക.

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

<img src="../../../translated_images/ml/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*നിശ്ചിത ഫോർമാറ്റ്, നീളം, ഘടന ആവശ്യങ്ങൾ പാലിക്കൽ*

## Using Existing Azure Resources

**ഡിപ്ലോയ്മെന്റ് സ്ഥിരീകരിക്കുക:**

Module 01-ൽ ഉണ്ടാക്കിയ `.env` ഫയൽ റൂട്ട് ഡയറക്ടറിയിൽ Azure ക്രെഡൻഷ്യലുകളോടെ ഉള്ളതായി ഉറപ്പാക്കുക:
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT കാണിക്കണം
```

**ആപ്ലിക്കേഷൻ ആരംഭിക്കുക:**

> **പരാമർശം:** Module 01-ൽ `./start-all.sh` ഉപയോഗിച്ച് എല്ലാ ആപ്ലിക്കേഷനുകളും ഇതിനകം ആരംഭിച്ചിട്ടുണ്ടെങ്കിൽ, ഈ മൊഡ്യൂൾ 8083 പോർട്ടിൽ പ്രവർത്തിയ്ക്കുകയാണ്. താഴെ നൽകിയിരിക്കുന്ന ആരംഭ കമാൻഡുകൾ ഒഴിവാക്കി നേരിട്ട് http://localhost:8083 സന്ദർശിക്കാവുന്നതാണ്.

**ഓപ്ഷൻ 1: Spring Boot ഡാഷ്ബോർഡ് ഉപയോഗിച്ച് (VS Code ഉപയോക്താക്കൾക്കായി ശുപാർശ ചെയ്യുന്നു)**

ഡെവ് കണ്ടയ്നറിലെ Spring Boot ഡാഷ്ബോർഡ് എക്സ്റ്റെൻഷൻ എല്ലാ Spring Boot ആപ്ലിക്കേഷനുകളും നിയന്ത്രിക്കാൻ ദൃശ്യമായ ഇന്റർഫേസ് നൽകുന്നു. VS Code-യുടെ ഇടതു Activity Bar-യിൽ (Spring Boot ഐക്കൺ നോക്കുക) ഇത് കാണാൻ കഴിയും.
From the Spring Boot Dashboard, you can:
- വർക്‌സ്‌പേസിലെ എല്ലാത്തിനും ലഭ്യമായ സ്പ്രിംഗ് ബൂട്ട് ആപ്പ്ലിക്കേഷനുകൾ കാണുക
- ഒരു ക്ലിക്കിൽ ആപ്പ്ലിക്കേഷനുകൾ ആരംഭിക്കുകയും നിർത്തുകയും ചെയ്യുക
- ആപ്പ്ലിക്കേഷൻ ലോഗുകൾ റിയൽ-ടൈമിൽ കാണുക
- ആപ്പ്ലിക്കേഷൻ നില നിരീക്ഷിക്കുക

"prompt-engineering" എന്നതിന്റെ പక్కയിൽ ഉള്ള പ്ലേ ബട്ടൺ ക്ലിക്കുചെയ്താൽ ഈ മോഡ്യൂൾ ആരംഭിക്കും, അല്ലെങ്കിൽ എല്ലാത്തും ഒരുമിച്ച് ആരംഭിക്കാം.

<img src="../../../translated_images/ml/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ഓപ്ഷൻ 2: ഷെൽ സ്ക്രിപ്റ്റുകൾ ഉപയോഗിച്ച്**

എല്ലാ വെബ് ആപ്പ്ലിക്കേഷനുകളും (മോഡ്യൂളുകൾ 01-04) ആരംഭിക്കുക:

**Bash:**
```bash
cd ..  # റൂട്ട് ഡയറക്ടറിയിൽ നിന്ന്
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # റൂട്ട് ഡയറക്ടറിയിൽ നിന്ന്
.\start-all.ps1
```

അല്ലെങ്കിൽ ഈ മോഡ്യൂൾ മാത്രം തുടങ്ങുക:

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

രണ്ടു സ്ക്രിപ്റ്റുകളും സ്വയം റൂട്ട് `.env` ഫയലിൽ നിന്നുള്ള പരിസ്ഥിതി ചോദ്യങ്ങൾ ലോഡ് ചെയ്യുകയും JAR ഇല്ലെങ്കിൽ നിർമ്മിക്കുകയും ചെയ്യും.

> **കുറിപ്പ്:** തുടങ്ങുന്നതിനു മുമ്പ് എല്ലാ മോഡ്യൂളുകളും കൈയ്യൊപ്പെത്തിക്കലpreferred ചെയ്യുകയാണെങ്കിൽ:
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

നിങ്ങളുടെ ബ്രൗസറിൽ http://localhost:8083 തുറക്കുക.

**അവസാനിപ്പിക്കാൻ:**

**Bash:**
```bash
./stop.sh  # ഈ മോഡ്യൂൾ മാത്രം
# അല്ലെങ്കിൽ
cd .. && ./stop-all.sh  # എല്ലാ മോഡ്യൂളുകളും
```

**PowerShell:**
```powershell
.\stop.ps1  # ഈ മൊഡ്യൂൾ മാത്രം
# അല്ലെങ്കിൽ
cd ..; .\stop-all.ps1  # എല്ലാ മൊഡ്യൂളുകളും
```

## ആപ്പ്ലിക്കേഷൻ സ്‌ക്രീൻഷോട്ടുകൾ

<img src="../../../translated_images/ml/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*പ്രധാന ഡാഷ്ബോർഡ്, എല്ലാ 8 പ്രോംപ്റ്റ് എൻജിനീയറിങ് പാടേണുകളും അവയുടെ സവിശേഷതകളും ഉപയോഗ കേസുകളും പ്രദർശിപ്പിക്കുന്നു*

## പാടേണുകൾ അന്വേഷിക്കൽ

വെബ് ഇന്റർഫെയ്സ് വ്യത്യസ്ത പ്രോംപ്റ്റിംഗ് തന്ത്രങ്ങൾ പരീക്ഷിക്കാൻ സുഖകരമായി സഹായിക്കുന്നു. ഓരോ പാടേണും വ്യത്യസ്ത പ്രശ്നങ്ങൾ പരിഹരിക്കുന്നു - ഏത് സമീപനം എപ്പോഴാണ് ഉത്തമം എന്ന് കാണാൻ പരീക്ഷിക്കുക.

### കുറവ് vs ഉയർന്ന ആഗ്രഹാശ

കുറവ് ആഗ്രഹാശ ഉപയോഗിച്ച് "200-ന്റെ 15% എത്ര?" പോലുള്ള എളുപ്പമുള്ള ചോദ്യങ്ങൾ ചോദിക്കുക. നിങ്ങൾക്ക് ഉടൻ നേരിട്ടുള്ള ഉത്തരം ലഭിക്കും. ഇപ്പോൾ "ഉയർന്ന ട്രാഫിക് APIയ്ക്ക് ക്യാഷിംഗ് തന്ത്രം രൂപകൽപ്പന ചെയ്യുക" എന്നു ഉയർന്ന ആഗ്രഹാശ ഉപയോഗിച്ച് ചോദിക്കുക. മോഡൽ എങ്ങനെ മന്ദഗതിയിലാണ് വിശദമായ വിവരണം നൽകുന്നത് എന്ന് കാണുക. ഒരേ മോഡൽ, സമാന ചോദ്യഘടന - എന്നാൽ പ്രോംപ്റ്റ് എത്രനാൾ ആലോചിക്കാമെന്ന് മോഡലിന് അറിയിക്കുന്നു.

<img src="../../../translated_images/ml/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*കുറഞ്ഞ ആലോചനയോടെയുള്ള ത്വരിത ഗണന*

<img src="../../../translated_images/ml/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*സമ്പൂർണ്ണമായ ക്യാഷിംഗ് തന്ത്രം (2.8MB)*

### ടാസ്‌ക് എക്സിക്യൂഷൻ (ടൂൾ പ്‌റീഅംബിൾസ്)

മൾട്ടി-സ്റ്റെപ്പ് വർക്ക്‌ഫ്ലോകൾക്ക് മുൻകൂട്ടി പദ്ധതിയിടലും പുരോഗതി വിവരണമുണ്ടാകലും പ്രയോജനമാണ്. മോഡൽ എന്ത് ചെയ്യും എന്ന് സംഗ്രഹിച്ചും ഓരോ ഘട്ടവും വിവരിച്ചതും തുടർന്ന് ഫലം സംഗ്രഹിച്ചതുമായ രീതിയാണ്.

<img src="../../../translated_images/ml/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ഘട്ടങ്ങൾ വിശദീകരിക്കുന്ന REST എൻഡ്പോയിൻറ് സൃഷ്ടിക്കൽ (3.9MB)*

### സ്വയംപരിശീലിക്കുന്ന കോഡ്

"ഒരു ഇമെയിൽ ശരിവെക്കൽ സേവനം സൃഷ്ടിക്കൂ" എന്ന് പറയൂ. സാധാരണ കോഡ് സൃഷ്ടിച്ച് നിർത്തുന്നതിന് പകരം, മോഡൽ കോഡ് സൃഷ്ടിക്കുകയും ഗുണമേന്മാ നിബന്ധനകളുമായി വിലയിരുത്തുകയും ദുർബലതകൾ കണ്ടെത്തുകയും മെച്ചപ്പെടുത്തുകയും ചെയ്യും. കോഡ് പ്രൊഡക്ഷൻ നിലവാരത്തിൽ എത്തുന്നത് വരെ മോഡൽ ആവർത്തിച്ച് കാണിക്കും.

<img src="../../../translated_images/ml/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*പൂർണ്ണ ഇമെയിൽ ശരിവെക്കൽ സേവനം (5.2MB)*

### ഘടനാത്മക വിശകലനം

കോഡ് റിവ്യൂകൾക്ക് സ്ഥിരമായ മൂല്യനിർണയ ഘടനകൾ വേണം. മോഡൽ കൃത്യത, രീതികൾ, പ്രകടനം, സുരക്ഷ തുടങ്ങിയ സ്ഥിരം വിഭാഗങ്ങൾ ഉപയോഗിച്ച് കോഡ് വിശകലനം ചെയ്യുന്നു.

<img src="../../../translated_images/ml/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ഘടനാപരമായ കോഡ് റിവ്യൂ*

### മൾട്ടി-ടേൺ ചാറ്റ്

"Spring Boot എന്താണ്?" എന്ന് ചോദിച്ച് ഉടനെ "ഒരു ഉദാഹരണം കാണിക്കുക" എന്നും ചോദിക്കുക. മോഡൽ നിങ്ങളുടെ ആദ്യ ചോദ്യവും ഓർക്കുകയും പ്രത്യേകമായി Spring Boot ഉദാഹരണം നൽകുകയും ചെയ്യും. മെമ്മറി ഇല്ലാതെ, രണ്ടാം ചോദ്യമാകുകയേ വേഗം വളരെ സങ്കീർണമാകുമായിരുന്നേനെ.

<img src="../../../translated_images/ml/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*ചോദ്യങ്ങൾക്കിടയിലെ സമഗ്ര സ്മൃതി സംരക്ഷണം*

### ഘട്ടം-ഘട്ടമായി ചിന്തിക്കൽ

ഒരു ഗണിത പ്രശ്നം തിരഞ്ഞെടുത്ത് ഘട്ടം-ഘട്ടമായി ചിന്തിക്കൽയും കുറവ് ആഗ്രഹാശയും പരീക്ഷിക്കുക. കുറവ് ആഗ്രഹാശ ഉടൻ ഉത്തരം നൽകും - വേഗം എന്നാൽ അപൂർണമായി. ഘട്ടം-ഘട്ടം നിങ്ങൾക്ക് കണക്കുകളും തീരുമാനങ്ങളും കാണിക്കുന്നു.

<img src="../../../translated_images/ml/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*ഘടകങ്ങളോടെ ഗണിത പ്രശ്നം*

### നിയന്ത്രിത ഔട്ട്പുട്ട്

നിശ്ചിത ഫോർമാറ്റുകൾ അല്ലെങ്കിൽ വാക്കുകളുടെ എണ്ണം ആവശ്യമായപ്പോൾ ഈ പാടേൺ കർശനമായ പാലനമാണ് നിർബന്ധിക്കുന്നത്. പത്തുo വാക്ക് കൂടുന്നത് നേരിട്ടുള്ള ചുരുക്കം ബുള്ളറ്റ് പോയിന്റുകളുടെ രൂപത്തിൽ നിർമ്മിക്കുക.

<img src="../../../translated_images/ml/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ഫോർമാറ്റ് നിയന്ത്രണമുള്ള മെഷീൻ ലേർണിംഗ് ചുരുക്കം*

## നിങ്ങൾ യഥാർത്ഥത്തിൽ പഠിക്കുന്നതു

**ചിന്തനശേഷി മാറ്റം വരുത്തുന്നു**

GPT-5.2 പ്രോംപ്റ്റുകൾ വഴി കംപ്യൂട്ടേഷണൽ ശ്രമം നിയന്ത്രിക്കാൻ അനുവദിക്കുന്നു. കുറഞ്ഞ ശ്രമം കുറഞ്ഞ അന്വേഷണത്തോടെ വേഗം മറുപടി നൽകുന്നു. ഉയർന്ന ശ്രമം മോഡൽ ആഴത്തിലുള്ള ചിന്തനം നടത്തുന്നു. നിങ്ങൾ ടാസ്കിന്റെ സങ്കീർണതയ്ക്ക് അനുയായമായ ശ്രമം നൽകാനുള്ള പഠനം ചെയ്യുന്നു - ലളിതമായ ചോദ്യങ്ങളിൽ സമയം കളയരുത്, എന്നാൽ സങ്കീർണ തീരുമാനങ്ങളിൽ ഉറ്റുനോക്കണം.

**ഘടന പെരുമാറ്റത്തിനെ നയിക്കുന്നു**

പ്രോംപ്റ്റിലെ XML ടാഗുകൾ കാണുന്നുണ്ടോ? അവ അലങ്കാരങ്ങൾ അല്ല. മോഡലുകൾ ഘടനീകൃത മാർഗനിർദ്ദേശങ്ങൾ സ്വതന്ത്ര വാചകത്തേക്കാൾ വിശ്വസനീയമായി പിന്തുടരുന്നു. മൾട്ടി-സ്റ്റെപ്പ് പ്രക്രിയകൾ അല്ലെങ്കിൽ സങ്കീർണ ലാജിക്ക് ആവശ്യമുള്ളപ്പോൾ, ഘടന മോഡലിന് ഇപ്പോൾ എവിടെയാണ് എന്നും തുടർന്നു എന്താണ് ചെയ്യേണ്ടതെന്നും നിരീക്ഷിക്കാൻ സഹായിക്കുന്നു.

<img src="../../../translated_images/ml/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*സൂക്ഷ്മ വിഭാഗങ്ങളുള്ള നല്ലഘടനയിലുള്ള പ്രോംപ്റ്റിന്റെ ഘടനയും XML-ശൈലി ക്രമീകരണവും*

**സ്വയം മൂല്യനിർണയത്തിലൂടെ ഗുണനിലവാരം**

സ്വയംപരിശീലിക്കുന്ന പാടേണുകൾ ഗുണമേന്മാ മാനദണ്ഡങ്ങൾ വ്യക്തമായി നിർവചിക്കുന്നവയാണ്. മോഡൽ "ശരി ചെയ്യുമെന്നാണ്" ആശിക്കുന്നത് പകരം, നിങ്ങൾ "ശരി" എന്നു പറയുന്നത് എന്താണെന്ന് വ്യക്തമാക്കുന്നു: ശരിയായ ലാജിക്, തെറ്റുനിവരണം, പ്രകടനം, സുരക്ഷ. മോഡൽ ഇതിന്റെ ഔട്ട്പുട്ട് സ്വയം വിലയിരുത്തുകയും മെച്ചപ്പെടുത്തുകയും കഴിയും. ഇത് കോഡ് സൃഷ്ടിയെ ലോട്ടറിയിൽ നിന്ന് പ്രക്രിയയാക്കി മാറ്റും.

**സന്ദർഭം പരിമിതമാണ്**

മൾട്ടി-ടേൺ സംഭാഷണങ്ങൾ ഓരോ അഭ്യർത്ഥനയിലും സന്ദേശ ചരിത്രം ഉൾപ്പെടുത്തിയാണ് പ്രവർത്തിക്കുന്നത്. പക്ഷേ പരമാവധി ടോക്കൺ പരിധിയുണ്ട്. സംഭാഷണങ്ങൾ വളർന്നുപോകുമ്പോൾ പാടുളള സമയത്ത് പ്രസക്തമായ സന്ദർഭം നിലനിർത്തുന്നതിനുള്ള തന്ത്രങ്ങൾ ആവശ്യമാണ്. ഈ മോഡ്യൂൾ മെമ്മറി എങ്ങനെ പ്രവർത്തിക്കുന്നുവെന്ന് കാണിക്കുന്നു; പിന്നീട് നിങ്ങൾക്കറിയാം എപ്പോൾ സംഗ്രഹിക്കണം, എപ്പോൾ മറക്കണം, എപ്പോൾ തിരിച്ച് നേടണം.

## അടുത്ത ഘട്ടങ്ങൾ

**അടുത്ത മോഡ്യൂൾ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**നാവിഗേഷൻ:** [← ഇത് കഴിഞ്ഞത്: മോഡ്യൂൾ 01 - പരിചയം](../01-introduction/README.md) | [പ്രധാനത്തിലേക്ക് തിരികെ](../README.md) | [അടുത്തത്: മോഡ്യൂൾ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ഡിസ്‌ക്ലെയിമർ**:  
ഈ ഡോക്യുമെന്റ് AI തർജ്ജമാ സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് തർജ്ജമ ചെയ്തത് ആണ്. നാം ശരിയായness ലക്ഷ്യമിട്ടുള്ളതായെങ്കിലും, യാന്ത്രിക തർജ്ജമയിൽ പിഴവുകൾ അല്ലെങ്കിൽ തെറ്റായതുകൾ ഉണ്ടാകാമെന്ന് ദയവായി മനസ്സിലാക്കുക. യഥാർത്ഥ ഭാഷയിൽ ഉള്ള അഭ്യന്തര ഡോക്യുമെന്റ് ആണ് പ്രാമാണിക ഉറവിടം എന്നതായി പരിഗണിക്കേണ്ടതാണ്. അത്യന്താപേക്ഷിത വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ തർജ്ജമ നിർദ്ദേശിക്കുന്നു. ഈ തർജ്മയിൽനിന്ന് ഉണ്ടാകുന്ന യാതൊരു ആശയക്കുഴപ്പത്തിനും അല്ലെങ്കിൽ തെറ്റായ വ്യാഖ്യാനങ്ങൾക്കും ഞങ്ങൾ ഉത്തരവാദിത്വം ഏറ്റെടുക്കില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->