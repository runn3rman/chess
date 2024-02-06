# My notes
Java Records - POJOs, class only exists to represent data


    A record generates all self and overide methods. 
    ex. public record Pet(int id, String name, String type){} would create 52 lines of code.
    - All fields are final
    - Simple constructor syntax
    - Automatic getters
    - Automatic @Override methods - equals, hashCode, toString.
    
    Records are immutable, so we just create a new one that we have done stuff on
    
Coding exam
    
    Closes at 5 pm on Friday
    Do all Prep before exam
    Spec
    Java doc
    One page of front/back notes. Paramaters, algorithms, etc. (make sure to do overrides, bind contructors to paramaters.
    Simplify code

Exceptions

    Objects/Classes
    Errors - Virtual machine, linkage (OutOfMemory, noClassFound)
    Exceptions - Runtime (nullPointer, indexOutOfBounds,), IOException
    
    Syntax
        Try {
            //Code that may throw exception
        } catch(SomeExceptionType){
            //Code to handle the exception
        } catch(OtherExceptionType)
            //Code to Handle Exception
        }

    - Go through steps and never come back to Try
    - Do "or" catches
    
    Finally blocks excecute no matter what