#!/usr/bin/env python3



def getCommand():
	result = '';

	result += 'Problem:problem/WTAProblem.txt\n'

	choice = int( input( '1: Genetic\n2: PSO\n3: ABC\n' ) )

	result += 'Algorithm:'
	
	if choice == 1:
		result += 'Genetic'
	elif choice == 2:
		result += 'PSO'
	elif choice == 3:
		result += 'ABC'
	else:
		print("Wrong number")
		return ''
	result += '\n'

	result += 'Time:10000\n'
	result += 'LossRate:' + input( 'Loss Rate: ' ) + '\n'
	result += 'Population:100\n'
	result += 'ExchangeInterval:' + input( 'Exchange Interval: ' ) + '\n'

	if choice == 1:
		result += 'CrossoverRate:' + input( 'Crossover Rate: ' ) + '\n'
		result += 'MutationRate:' + input( 'Mutation Rate: ' ) + '\n'
	elif choice == 2:
		result += 'Omega:' + input( 'Omega: ' ) + '\n'
		result += 'C1:' + input( 'C1: ' ) + '\n'
		result += 'C2:' + input( 'C2: ' ) + '\n'
	elif choice == 3:
		result += 'TrialLimit:' + input( 'Trial Limit: ' ) + '\n'

	return result



result = getCommand()

repeat = int( input( "Round: " ) )

print()
print('+--------+')
print('| RESULT |')
print('+--------+')
print( result )

f = open( 'experiment/single', 'w' )

for i in range( repeat ):
	if i != 0:
		f.write('--\n')
	f.write( result )
f.close()
