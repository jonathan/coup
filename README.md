# coup

This is a playground for the The Coup card game.

## Usage

Right now there isn't much here. Just a simple GA to evolve a
toy problem. The next step is to create a working Coup game.
I'll probably look into representing this as a FSM. The AI will
be simple agents coded up with a seq of probabilities of what
they will do. That seq will be the basis for evolving better AI.


## Examples

Right now only the GA works. You can prove that by starting a
repl with `lein repl` and doing the following:

```
coup.core=> (use 'ga.core)
nil
coup.core=> (ga.core/evolve)
{:chromosome (\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z), :fitness 26}
```

This shows that the GA is working. More logging needs to be added
for better stats tracking and debugging.

### Bugs


## License

Copyright © 2014 Jonathan Hicks

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
