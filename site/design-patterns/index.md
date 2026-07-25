---
layout: default
title: "Design Patterns"
---

# 🎨 Design Patterns

All 23 GoF patterns — fully implemented in Java with inline explanations and runnable demos.

**5 Creational &nbsp;·&nbsp; 7 Structural &nbsp;·&nbsp; 11 Behavioral**

---

## 🏗️ Creational Patterns

*Deal with object creation mechanisms, aiming to create objects in a manner suitable to the situation.*

| Pattern | Difficulty | Intent |
|---------|:----------:|--------|
| [Singleton]({{ '/design-patterns/singleton/' | relative_url }}) | 🟢 Easy | Ensure a class has only ONE instance and provide a global access point to it |
| [Factory Method]({{ '/design-patterns/factory-method/' | relative_url }}) | 🟡 Medium | Define an interface for creating an object, but let subclasses decide which class to instantiate |
| [Abstract Factory]({{ '/design-patterns/abstract-factory/' | relative_url }}) | 🟡 Medium | Provide an interface for creating families of related objects without specifying concrete classes |
| [Builder]({{ '/design-patterns/builder/' | relative_url }}) | 🟡 Medium | Separate the construction of a complex object from its representation |
| [Prototype]({{ '/design-patterns/prototype/' | relative_url }}) | 🟡 Medium | Create new objects by copying a prototypical instance |

---

## 🏛️ Structural Patterns

*Deal with object composition, creating relationships between objects to form larger structures.*

| Pattern | Difficulty | Intent |
|---------|:----------:|--------|
| [Adapter]({{ '/design-patterns/adapter/' | relative_url }}) | 🟢 Easy | Convert the interface of a class into another interface clients expect |
| [Bridge]({{ '/design-patterns/bridge/' | relative_url }}) | 🔴 Hard | Decouple an abstraction from its implementation so both can vary independently |
| [Composite]({{ '/design-patterns/composite/' | relative_url }}) | 🟡 Medium | Compose objects into tree structures to represent part-whole hierarchies |
| [Decorator]({{ '/design-patterns/decorator/' | relative_url }}) | 🟡 Medium | Attach additional responsibilities to an object dynamically |
| [Facade]({{ '/design-patterns/facade/' | relative_url }}) | 🟢 Easy | Provide a simplified, unified interface to a complex subsystem |
| [Flyweight]({{ '/design-patterns/flyweight/' | relative_url }}) | 🔴 Hard | Use sharing to support large numbers of fine-grained objects efficiently |
| [Proxy]({{ '/design-patterns/proxy/' | relative_url }}) | 🟡 Medium | Provide a surrogate or placeholder for another object to control access to it |

---

## 🔄 Behavioral Patterns

*Deal with communication and responsibility between objects.*

| Pattern | Difficulty | Intent |
|---------|:----------:|--------|
| [Chain of Responsibility]({{ '/design-patterns/chain-of-responsibility/' | relative_url }}) | 🟡 Medium | Give more than one object a chance to handle a request |
| [Command]({{ '/design-patterns/command/' | relative_url }}) | 🟡 Medium | Encapsulate a request as an object; support undo/redo and queuing |
| [Interpreter]({{ '/design-patterns/interpreter/' | relative_url }}) | 🔴 Hard | Define a grammar for a language and an interpreter for it |
| [Iterator]({{ '/design-patterns/iterator/' | relative_url }}) | 🟢 Easy | Sequentially access elements of a collection without exposing its representation |
| [Mediator]({{ '/design-patterns/mediator/' | relative_url }}) | 🟡 Medium | Define an object that encapsulates how a set of objects interact |
| [Memento]({{ '/design-patterns/memento/' | relative_url }}) | 🟡 Medium | Capture and externalize an object's internal state without violating encapsulation |
| [Observer]({{ '/design-patterns/observer/' | relative_url }}) | 🟡 Medium | Define a one-to-many dependency so dependents are notified of state changes |
| [State]({{ '/design-patterns/state/' | relative_url }}) | 🟡 Medium | Allow an object to alter its behavior when its internal state changes |
| [Strategy]({{ '/design-patterns/strategy/' | relative_url }}) | 🟡 Medium | Define a family of algorithms, encapsulate each one, and make them interchangeable |
| [Template Method]({{ '/design-patterns/template-method/' | relative_url }}) | 🟡 Medium | Define the skeleton of an algorithm in a base class, deferring some steps to subclasses |
| [Visitor]({{ '/design-patterns/visitor/' | relative_url }}) | 🔴 Hard | Represent an operation to be performed on object structure elements |

---

## Quick Reference — When to Use Which Pattern

| Problem | Pattern |
|---------|---------|
| Need exactly one instance | Singleton |
| Creating objects but don't know which subclass | Factory Method |
| Creating families of related objects | Abstract Factory |
| Building complex objects step-by-step | Builder |
| Copying expensive objects | Prototype |
| Making incompatible interfaces work together | Adapter |
| Abstraction and implementation vary independently | Bridge |
| Treating individual objects and groups uniformly | Composite |
| Adding behavior without subclassing | Decorator |
| Simplifying a complex subsystem | Facade |
| Millions of similar fine-grained objects | Flyweight |
| Controlling access to an object | Proxy |
| Multiple objects can handle a request | Chain of Responsibility |
| Parameterize / queue / undo requests | Command |
| Interpreting sentences in a language | Interpreter |
| Traversing a collection without exposing internals | Iterator |
| Reducing direct communication between objects | Mediator |
| Saving and restoring object state | Memento |
| Notifying multiple objects of state changes | Observer |
| Object behavior changes with its state | State |
| Interchangeable algorithms at runtime | Strategy |
| Skeleton algorithm with variable steps | Template Method |
| New operations on a stable class hierarchy | Visitor |
