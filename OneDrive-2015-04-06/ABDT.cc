#include <iostream>
#include <string.h>
#include <vector>
#include <set>
#include <iomanip>
#include <map>
#include <stdlib.h>

using namespace std;

//#define DEBUG

struct Mypair{
	string A;
	string B;
	string name;
};


bool operator==(const Mypair &lhs,const Mypair &rhs){
	Mypair * temp1 = (Mypair *)&lhs;
	Mypair * temp2 = (Mypair *)&rhs;
	return temp1->A == temp2->A || temp1->B == temp2->B;
}

bool operator<(const Mypair &lhs, const Mypair &rhs){
	Mypair * temp1 = (Mypair *)&lhs;
	Mypair * temp2 = (Mypair *)&rhs;
	if(temp1->A < temp2->A){
		return true;
	}else if(temp1->A == temp2->A && temp1->B < temp2->B){
		return true;
	}else{
		return false;
	}

}

bool operator>(const Mypair &lhs, const Mypair &rhs){
//	cout << 102 << endl;
	return false;
}

bool operator<=(const Mypair &lhs, const Mypair &rhs){
//	cout << 103 << endl;
	return true;
}

bool operator>=(const Mypair &lhs, const Mypair &rhs){
	return false;
}





static int SUPPORT;
static int CONFIDENCE;
static map<string,set<string> > mapping;
static set<Mypair> funcPair;

//static unordered_map<string,vector<Mypair>> funcPair;


void debugMappingFunc(){

#ifdef DEBUG
	cout << "Producing mapping functions..." << endl;
	for(map<string,set<string>>::iterator it = mapping.begin(); it != mapping.end(); ++it){
		cout << "parentName: " << it->first << endl;
		for(auto it2 = it->second.begin(); it2 != it->second.end(); it2++){
			cout << "childName: " << *it2 << endl;
		}
	}

#endif

}

void debugFuncPair(){

#ifdef DEBUG
	cout << "Producing function pairs..." << endl;
	for(set<Mypair>::iterator it = funcPair.begin(); it != funcPair.end(); ++it){
		cout << "A: " << it->A;
		cout << " B: " << it->B;
		cout << " Parent: " << it->name << endl;
	}

#endif

/*
#ifdef DEBUG
	cout << "Producing function pairs..." << endl;
	for(auto it = funcPair.begin(); it != funcPair.end(); ++it){
		cout << "parentName: " << it->first << endl;
		for(auto it2 = it->second.begin(); it2 != it->second.end(); it2++){
			cout << "A: " << it2->A;
			cout << "B: " << it2->B << endl;
		}
	}

#endif
*/
}


//parse the call graph and store it to 
//hash tables
void parseCallGraph(){
		//parsing call graph

	string x;

	char *pch;
	char input[1024];
	string currentParentName = "";
	bool shouldUpdateParentName = true;
	while(!cin.eof()){
		getline(cin,x);

		strncpy(input, x.c_str(),sizeof(input));
		input[sizeof(input) -1] = 0;

//		cout << x << endl;

		if(x == ""){
			shouldUpdateParentName = true;
			continue;
		
		}else{

			if(shouldUpdateParentName){
				//graph node
				int start = -1;
				int end = -1;
				for(int i = 0; i < strlen(input);i++){
					if(input[i] == '\'' && start != -1){
						end = i;
					}else if(input[i] == '\''){
						start = i;
					}
				}

				if(start != -1 && end != -1) {
					currentParentName = x.substr(start, end-start+1);
					if(currentParentName != "")
						mapping[currentParentName.substr(1,currentParentName.length()-2)] = set<string>();
			//		funcPair[currentParentName] = {};
				}	
			
			#ifdef DEBUG
				cout << "currentParentName: " 
						<< currentParentName << endl;
			#endif

				

			}else{

				int start = -1;
				int end = -1;
				for(int i = 0; i < strlen(input);i++){
					if(input[i] == '\'' && start != -1){
						end = i;
					}else if(input[i] == '\''){
						start = i;
					}
				}

				string currentChildName;

				if(start != -1 && end != -1){
					currentChildName = x.substr(start, end-start+1);
					if(currentParentName != "")
						mapping[currentParentName.substr(1,currentParentName.length()-2)].insert(currentChildName.substr(1,currentChildName.length()-2));
				}	
			
			#ifdef DEBUG	
					cout << "currentChildName: " 
						<< currentChildName << endl;
			#endif
				


			}
			shouldUpdateParentName = false;

			
		}

	}
}

//produce function pairs from hash table

void produceFunctionPair(){
	for(map<string, set<string> >::iterator it = mapping.begin(); it != mapping.end(); ++it){
		if(it->second.size() <= 1) continue;

		for(set<string>::iterator it2 = it->second.begin(); it2 != it->second.end(); ++it2){
			set<string>::iterator temp = it2;
		//	cout << *it2 << endl;
			if(++temp == it->second.end()) continue;
		//	cout << 2 << endl;
			for(set<string>::iterator it3 = temp; it3 != it->second.end(); it3++){
				Mypair tempP = {*it2, *it3, it->first};
		//		cout << 5 << endl;
			//	funcPair[it->first].push_back(tempP);
				funcPair.insert(tempP);
		//		cout << 3 << endl;
			}
		}
	}
}

//find potential bugs from function pair array

void findBugs(){
	#ifdef DEBUG
	cout << "Producing bugs report" << endl;
	#endif

	for(set<Mypair>::iterator it = funcPair.begin(); it != funcPair.end(); ++it){
		//this function does not have function fair
		int numOccurenceForA = 0;
		int numOccurenceForB = 0;
		int numOccurenceForBoth = 0;

		set<string> parentNameForA = set<string>();
		set<string> parentNameForB = set<string>();

		for(map<string,set<string> >::iterator it2 = mapping.begin(); 
				it2 != mapping.end(); ++it2){
			if(it2->second.find(it->A) != it2->second.end() &&
				it2->second.find (it->B) != it2->second.end()){
				//function pair found
				numOccurenceForBoth++;
				numOccurenceForA++;
				numOccurenceForB++;
			}else if(it2->second.find(it->A) != it2->second.end()){
				numOccurenceForA++;
				parentNameForA.insert(it2->first);
			}else if(it2->second.find(it->B) != it2->second.end()){
				numOccurenceForB++;
				parentNameForB.insert(it2->first);
			}else{
				continue;
			}

		}


		if(numOccurenceForA!=0 && numOccurenceForBoth >= SUPPORT &&
			(numOccurenceForBoth * 100 / numOccurenceForA)
			 >= CONFIDENCE){
			#ifdef DEBUG
				cout << "bug found in ";
				cout << it->name << ": " << it->A << endl;
			#endif

/*			printf("bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%\n",it->A, it->name,
						it->A, it->B, numOccurenceForA,
						(numOccurenceForBoth * 100 / numOccurenceForB));
*/
			for(set<string>::iterator it2 = parentNameForA.begin(); it2 != parentNameForA.end(); ++it2){
				cerr << fixed;
				cerr << "bug: " << it->A << " in " << *it2 << ", pair: (";
				cerr << it->A << ", " << it->B<<"), support: " << numOccurenceForBoth;
					cerr << ", confidence: "<< setprecision(2)  << (float)numOccurenceForBoth * 100.00 / (float)numOccurenceForA;
					cerr << "%" << endl;
			}
		}



		if(numOccurenceForB!= 0 && numOccurenceForBoth >= SUPPORT &&
			(numOccurenceForBoth * 100 / numOccurenceForB)
			 >= CONFIDENCE){
			#ifdef DEBUG
			cout << "bug found in ";
			if(it->name == "")
				cout << "null fucntion: " << it->B << endl; 
			else
				cout << it->name << ": " << it->B << endl;
			#endif

			//bug output
	/*		printf("bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%\n",it->B, it->name,
						it->A, it->B, numOccurenceForB,
						(numOccurenceForBoth * 100 / numOccurenceForB));
	*/
			for(set<string>::iterator it2 = parentNameForB.begin(); it2 != parentNameForB.end(); ++it2){
				cerr << fixed;
				cerr << "bug: " << it->B << " in " << *it2 << ", pair: (";
				cerr << it->A << ", " << it->B <<"), support: " << numOccurenceForBoth;
					cerr << ", confidence: " <<  setprecision(2) << (float)numOccurenceForBoth * 100.00 / numOccurenceForB;
					cerr << "%" << endl;
			}	

		}

		




	}

}



int main(int argc, char* argv[]){


	if(argc == 2 ){
		SUPPORT = 3;
		CONFIDENCE = 65;
	}else{
		SUPPORT = atoi(argv[2]);
		CONFIDENCE = atoi(argv[3]);
		//cout << SUPPORT << endl;
		//cout << CONFIDENCE << endl;
	}

	#ifdef DEBUG
	cout << "supprot: " << SUPPORT << endl;
	cout << "confidence: " << CONFIDENCE << endl;
	#endif

	parseCallGraph();
	debugMappingFunc();
	produceFunctionPair();
	debugFuncPair();
	findBugs();

	return 1;
}